package com.aurionpro.bank.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.model.AccountDAO;
import com.aurionpro.bank.model.DBConnection;
import com.aurionpro.bank.model.Transaction;
import com.aurionpro.bank.util.ReceiptPrinter;

public class BankFacade {
    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();
    private final AccountDAO accountDAO = new AccountDAO();

    // Create Account
    public int createNewAccount(String name, String pin, BigDecimal deposit)
            throws SQLException, InvalidTransactionException {
        return accountService.createAccount(name, pin, deposit);
    }

    // User Login
    public Account loginUser(int accNo, String pin) throws SQLException {
        return accountService.login(accNo, pin);
    }

    // Get Accounts
    public List<Account> getAccounts(String type) throws SQLException {
        return accountService.getAccounts(type);
    }
 // 🔹 Get single account by ID
    public Account getAccount(int accNo) throws SQLException {
        return accountService.getAccount(accNo);
    }


 // Deposit
    public boolean deposit(int accNo, BigDecimal amt, String pin) throws SQLException, InvalidTransactionException {
        boolean success = accountService.deposit(accNo, amt, pin);
        if (success) {
            Account acc = accountService.getAccount(accNo);
            ReceiptPrinter.print(acc, "DEPOSIT", amt, acc.getBalance());
            try (Connection conn = DBConnection.getConnection()) {
                new TransactionDAO().addTransactionSingleAccount(accNo, amt, "DEPOSIT", conn);
            }
        }
        return success;
    }

    // Withdraw
    public boolean withdraw(int accNo, BigDecimal amt, String pin) throws SQLException, InvalidTransactionException {
        boolean success = accountService.withdraw(accNo, amt, pin);
        if (success) {
            Account acc = accountService.getAccount(accNo);
            ReceiptPrinter.print(acc, "WITHDRAW", amt, acc.getBalance());
            try (Connection conn = DBConnection.getConnection()) {
                new TransactionDAO().addTransactionSingleAccount(accNo, amt, "WITHDRAW", conn);
            }
        }
        return success;
    }


    // 🔹 Transfer Funds
    public boolean transfer(int fromId, int toId, BigDecimal amt, String pin) throws SQLException, InvalidTransactionException {
        if (fromId == toId) {
            throw new InvalidTransactionException("❌ Sender and Receiver cannot be the same.");
        }

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            Account sender = accountService.getAccount(fromId);
            Account receiver = accountService.getAccount(toId);

            if (sender == null || receiver == null) {
                throw new InvalidTransactionException("One of the accounts not found.");
            }
            if (!sender.getPin().equals(pin)) {
                throw new InvalidTransactionException("Invalid PIN.");
            }
            if (sender.getBalance().compareTo(amt) < 0) {
                throw new InvalidTransactionException("Insufficient funds.");
            }

            // Update balances
            sender.setBalance(sender.getBalance().subtract(amt));
            accountDAO.update(sender);

            receiver.setBalance(receiver.getBalance().add(amt));
            accountDAO.update(receiver);

            // Record Transaction
            transactionService.recordTransaction(fromId, toId, amt, "TRANSFER");

            // ✅ Print Transfer Receipt
            ReceiptPrinter.printTransferReceipt(sender, receiver, amt, sender.getBalance(), receiver.getBalance());

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }


    // Update PIN
    public boolean updatePin(int accNo, String oldPin, String newPin)
            throws SQLException, InvalidTransactionException {
        return accountService.updatePin(accNo, oldPin, newPin);
    }

    // Set Account Status
    public boolean setAccountStatus(int accNo, boolean status) throws SQLException {
        return accountService.setAccountStatus(accNo, status);
    }

    // Close Account
    public boolean closeAccount(int accNo) throws SQLException {
        return accountService.closeAccount(accNo);
    }

    // Get All Transactions
    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionService.getAllTransactions();
    }

    // Get Transactions for Account
    public List<Transaction> getTransactionsForAccount(int accNo) throws SQLException {
        return transactionService.getTransactionsForAccount(accNo);
    }
}
