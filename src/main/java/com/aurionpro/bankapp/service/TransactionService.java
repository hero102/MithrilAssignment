package com.aurionpro.bankapp.service;

import com.aurionpro.bankapp.dto.PassbookResponse;
import com.aurionpro.bankapp.dto.TransactionRequest;
import com.aurionpro.bankapp.dto.TransferTransactionResponse;
import com.aurionpro.bankapp.entity.Transaction;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface TransactionService {

    Transaction create(Long accountId, TransactionRequest req) throws AccessDeniedException;

    TransferTransactionResponse transfer(Long accountId, TransactionRequest req) throws AccessDeniedException;

    List<Transaction> getByAccount(Long accountId);

    List<Transaction> getByCustomer(Long customerId);

    PassbookResponse getPassbook(Long accountId);

    void sendPassbookEmail(Long accountId);
    
    public List<Transaction> getAllTransactions();


    // Add this helper
    default String getCustomerNameByUsername(String username) {
        throw new UnsupportedOperationException("Implement in service");
    }
}
