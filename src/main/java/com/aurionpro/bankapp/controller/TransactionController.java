package com.aurionpro.bankapp.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.bankapp.dto.EntityMapper;
import com.aurionpro.bankapp.dto.TransactionRequest;
import com.aurionpro.bankapp.dto.TransactionResponse;
import com.aurionpro.bankapp.dto.TransferTransactionResponse;
import com.aurionpro.bankapp.entity.Transaction;
import com.aurionpro.bankapp.service.AuthService;
import com.aurionpro.bankapp.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AuthService authService;

    public TransactionController(TransactionService transactionService, AuthService authService) {
        this.transactionService = transactionService;
        this.authService = authService;
    }

    // ------------------ CREATE TRANSACTION ------------------
    @PostMapping("/account/{accountId}")
    public ResponseEntity<?> createTransaction(@PathVariable Long accountId,
                                               @RequestBody TransactionRequest req) throws AccessDeniedException {
        authService.checkAccountOwnership(accountId);

        if ("transfer".equalsIgnoreCase(req.getTranstype())) {
            TransferTransactionResponse transferResponse = transactionService.transfer(accountId, req);
            return ResponseEntity.ok(transferResponse);
        } else {
            Transaction txn = transactionService.create(accountId, req);
            String loggedInCustomerName = authService.getLoggedInUsername();
            return ResponseEntity.ok(EntityMapper.toTransactionResponse(txn, loggedInCustomerName));
        }
    }

    // ------------------ GET TRANSACTIONS BY ACCOUNT ------------------
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(@PathVariable Long accountId)
            throws AccessDeniedException {
        authService.checkAccountOwnership(accountId);

        String loggedInCustomerName = authService.getLoggedInUsername();
        List<TransactionResponse> txns = transactionService.getByAccount(accountId).stream()
                .map(tx -> EntityMapper.toTransactionResponse(tx, loggedInCustomerName))
                .collect(Collectors.toList());

        return ResponseEntity.ok(txns);
    }

    // ------------------ GET TRANSACTIONS BY CUSTOMER ------------------
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCustomer(@PathVariable Long customerId)
            throws AccessDeniedException {
        authService.checkAccess(customerId);

        String loggedInCustomerName = authService.getLoggedInUsername();
        List<TransactionResponse> txns = transactionService.getByCustomer(customerId).stream()
                .map(tx -> EntityMapper.toTransactionResponse(tx, loggedInCustomerName))
                .collect(Collectors.toList());

        return ResponseEntity.ok(txns);
    }

    // ------------------ EMAIL PASSBOOK ------------------
    @GetMapping("/passbook/email/{accountId}")
    public ResponseEntity<String> emailPassbook(@PathVariable Long accountId) throws AccessDeniedException {
        authService.checkAccountOwnership(accountId);
        transactionService.sendPassbookEmail(accountId);
        return ResponseEntity.ok("Passbook emailed successfully to customer.");
    }

    // ------------------ GET ALL TRANSACTIONS (ADMIN ONLY) ------------------
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> txns = transactionService.getAllTransactions().stream()
                .map(tx -> EntityMapper.toTransactionResponse(
                        tx,
                        tx.getCustomer() != null ? tx.getCustomer().getName() : "Unknown"
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(txns);
    }
}
