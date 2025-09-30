package com.aurionpro.bankapp.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.aurionpro.bankapp.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class AccountResponse {
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private boolean active;
    private Long customerId;

    // ✅ Constructor with logged-in customer name for proper transaction mapping
    public AccountResponse(Account account, String loggedInCustomerName) {
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.active = account.isActive();
        this.customerId = account.getCustomer() != null ? account.getCustomer().getId() : null;
    }

    // Legacy constructor using default TransactionResponse mapping
    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.active = account.isActive();
        this.customerId = account.getCustomer() != null ? account.getCustomer().getId() : null;


    }
}
