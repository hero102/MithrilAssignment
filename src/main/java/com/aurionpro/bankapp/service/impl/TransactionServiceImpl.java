package com.aurionpro.bankapp.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.bankapp.dto.PassbookResponse;
import com.aurionpro.bankapp.dto.TransactionRequest;
import com.aurionpro.bankapp.dto.TransactionResponse;
import com.aurionpro.bankapp.dto.TransferTransactionResponse;
import com.aurionpro.bankapp.entity.Account;
import com.aurionpro.bankapp.entity.Transaction;
import com.aurionpro.bankapp.exception.ResourceNotFoundException;
import com.aurionpro.bankapp.repository.AccountRepository;
import com.aurionpro.bankapp.repository.CustomerRepository;
import com.aurionpro.bankapp.repository.TransactionRepository;
import com.aurionpro.bankapp.service.EmailService;
import com.aurionpro.bankapp.service.TransactionService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository txnRepo;
    private final AccountRepository accountRepo;
    private final EmailService emailService;
    private final CustomerRepository customerRepo;

    public TransactionServiceImpl(TransactionRepository txnRepo, AccountRepository accountRepo,
                                  EmailService emailService, CustomerRepository customerRepo) {
        this.txnRepo = txnRepo;
        this.accountRepo = accountRepo;
        this.emailService = emailService;
        this.customerRepo = customerRepo;
    }

    // ------------------ ADMIN: GET ALL ------------------
    @Override
    public List<Transaction> getAllTransactions() {
        return txnRepo.findAll();
    }

    // ------------------ CREATE (DEBIT / CREDIT) ------------------
    @Override
    public Transaction create(Long accountId, TransactionRequest req) throws AccessDeniedException {
        Account account = getAccount(accountId);
        validateAmount(account, req.getAmount());

        Transaction txn = new Transaction();
        txn.setTranstype(req.getTranstype().toLowerCase());
        txn.setAmount(req.getAmount());
        txn.setDateTime(LocalDateTime.now());
        txn.setRemarks(req.getRemarks());
        txn.setAccount(account);
        txn.setCustomer(account.getCustomer());

        switch (req.getTranstype().toLowerCase()) {
            case "debit":
                if (account.getBalance().compareTo(req.getAmount()) < 0) {
                    throw new AccessDeniedException("Insufficient funds");
                }
                account.setBalance(account.getBalance().subtract(req.getAmount()));
                txn.setSenderName(account.getCustomer().getName());
                txn.setReceiverName("Bank");
                break;

            case "credit":
                account.setBalance(account.getBalance().add(req.getAmount()));
                txn.setSenderName("Bank");
                txn.setReceiverName(account.getCustomer().getName());
                break;

            default:
                throw new AccessDeniedException("Invalid transaction type: " + req.getTranstype());
        }

        accountRepo.save(account);
        txnRepo.save(txn);
        sendTransactionEmail(txn);

        return txn;
    }

    // ------------------ TRANSFER ------------------
    @Override
    public TransferTransactionResponse transfer(Long accountId, TransactionRequest req) throws AccessDeniedException {
        Account source = getAccount(accountId);
        Account dest = accountRepo.findByAccountNumber(req.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (source.getId().equals(dest.getId())) {
            throw new IllegalArgumentException("Self-transfer not allowed");
        }

        validateAmount(source, req.getAmount());
        if (source.getBalance().compareTo(req.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Update balances
        source.setBalance(source.getBalance().subtract(req.getAmount()));
        dest.setBalance(dest.getBalance().add(req.getAmount()));

        // Create debit txn
        Transaction debitTxn = new Transaction();
        debitTxn.setTranstype("transfer-debit");
        debitTxn.setAmount(req.getAmount());
        debitTxn.setDateTime(LocalDateTime.now());
        debitTxn.setRemarks("Transfer to " + dest.getAccountNumber());
        debitTxn.setAccount(source);
        debitTxn.setCustomer(source.getCustomer());
        debitTxn.setSenderName(source.getCustomer().getName());
        debitTxn.setReceiverName(dest.getCustomer().getName());

        // Create credit txn
        Transaction creditTxn = new Transaction();
        creditTxn.setTranstype("transfer-credit");
        creditTxn.setAmount(req.getAmount());
        creditTxn.setDateTime(LocalDateTime.now());
        creditTxn.setRemarks("Transfer from " + source.getAccountNumber());
        creditTxn.setAccount(dest);
        creditTxn.setCustomer(dest.getCustomer());
        creditTxn.setSenderName(source.getCustomer().getName());
        creditTxn.setReceiverName(dest.getCustomer().getName());

        accountRepo.save(source);
        accountRepo.save(dest);
        txnRepo.save(debitTxn);
        txnRepo.save(creditTxn);

        sendTransactionEmail(debitTxn);
        sendTransactionEmail(creditTxn);

        return new TransferTransactionResponse(
                new TransactionResponse(debitTxn),
                new TransactionResponse(creditTxn)
        );
    }

    // ------------------ GET BY ACCOUNT ------------------
    @Override
    public List<Transaction> getByAccount(Long accountId) {
        return txnRepo.findByAccount_Id(accountId);
    }

    // ------------------ GET BY CUSTOMER ------------------
    @Override
    public List<Transaction> getByCustomer(Long customerId) {
        return txnRepo.findByCustomer_Id(customerId);
    }

    // ------------------ PASSBOOK ------------------
    @Override
    public PassbookResponse getPassbook(Long accountId) {
        Account account = getAccount(accountId);
        List<TransactionResponse> txns = txnRepo.findByAccount_Id(accountId).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
        return new PassbookResponse(account.getBalance(), txns);
    }

    @Override
    public void sendPassbookEmail(Long accountId) {
        Account account = getAccount(accountId);
        PassbookResponse passbook = getPassbook(accountId);

        try {
            byte[] pdfBytes = generatePassbookPdf(account, passbook);
            emailService.sendEmailWithAttachment(
                    account.getCustomer().getUser().getEmail(),
                    "Your Passbook PDF",
                    "Dear " + account.getCustomer().getName() + ",\n\nPlease find attached your passbook.",
                    "passbook.pdf",
                    pdfBytes
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =================== PDF ===================
    public byte[] generatePassbookPdf(Account account, PassbookResponse passbook) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Header
        Font bankFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph bankName = new Paragraph("HeroKumar Bank", bankFont);
        bankName.setAlignment(Element.ALIGN_CENTER);
        document.add(bankName);

        Paragraph title = new Paragraph("Account Passbook Statement",
                FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.DARK_GRAY));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);

        // Account Info
        Paragraph accInfo = new Paragraph(
                "Account Number: " + account.getAccountNumber() + "\n" +
                        "Account Type: " + account.getAccountType() + "\n" +
                        "Customer Name: " + account.getCustomer().getName(),
                FontFactory.getFont(FontFactory.HELVETICA, 12)
        );
        accInfo.setSpacingAfter(10f);
        document.add(accInfo);

        // Table with 7 cols
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setWidths(new float[]{1f, 2f, 2f, 2f, 2f, 2f, 3f});

        String[] headers = {"ID", "Type", "Amount", "Date", "Sender", "Receiver", "Remarks"};
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            table.addCell(cell);
        }

        boolean alternate = false;
        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
        for (TransactionResponse txn : passbook.getTransactions()) {
            BaseColor rowColor = alternate ? BaseColor.LIGHT_GRAY : BaseColor.WHITE;
            table.addCell(createCell(String.valueOf(txn.getId()), rowFont, rowColor));
            table.addCell(createCell(txn.getTranstype(), rowFont, rowColor));
            table.addCell(createCell(String.valueOf(txn.getAmount()), rowFont, rowColor, Element.ALIGN_RIGHT));
            table.addCell(createCell(txn.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), rowFont, rowColor));
            table.addCell(createCell(txn.getSenderName() != null ? txn.getSenderName() : "-", rowFont, rowColor));
            table.addCell(createCell(txn.getReceiverName() != null ? txn.getReceiverName() : "-", rowFont, rowColor));
            table.addCell(createCell(txn.getRemarks(), rowFont, rowColor));
            alternate = !alternate;
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph footer = new Paragraph("This is a computer-generated passbook from HeroKumar Bank.",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    // =================== HELPERS ===================
    private void validateAmount(Account account, BigDecimal amount) {
        if (!account.isActive()) throw new IllegalArgumentException("Account is deactivated");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be > 0");
    }

    private Account getAccount(Long accountId) {
        return accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private void sendTransactionEmail(Transaction tx) {
        String to = tx.getCustomer().getUser().getEmail();
        String subject = "Transaction Notification";
        String body = String.format(
                "Dear %s,\n\nTransaction Type: %s\nAmount: %s\nDate: %s\nSender: %s\nReceiver: %s\nBalance: %s",
                tx.getCustomer().getName(),
                tx.getTranstype(),
                tx.getAmount(),
                tx.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                tx.getSenderName(),
                tx.getReceiverName(),
                tx.getAccount().getBalance()
        );
        emailService.sendSimpleMessage(to, subject, body);
    }

    private PdfPCell createCell(String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell createCell(String text, Font font, BaseColor bgColor, int align) {
        PdfPCell cell = createCell(text, font, bgColor);
        cell.setHorizontalAlignment(align);
        return cell;
    }
}
