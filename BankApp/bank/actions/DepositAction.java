package com.aurionpro.bank.actions;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class DepositAction {
    private final BankFacade bank;

    public DepositAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int accountId = InputHandler.getInt("Enter Account ID: ");
        BigDecimal amount = InputHandler.getAmount("Enter deposit amount (₹): ");
        String pin = InputHandler.getString("Enter your 4-digit PIN: ");

        try {
            if (bank.deposit(accountId, amount, pin)) {
                Account acc = bank.getAccount(accountId);
                System.out.println("✅ Deposit successful! New Balance: ₹" + acc.getBalance());
            }
        } catch (InvalidTransactionException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}
