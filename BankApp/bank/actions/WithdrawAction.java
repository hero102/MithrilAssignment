package com.aurionpro.bank.actions;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class WithdrawAction {
    private final BankFacade bank;

    public WithdrawAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int accountId = InputHandler.getInt("Enter Account ID: ");
        BigDecimal amount = InputHandler.getAmount("Enter withdrawal amount (₹): ");

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            String pin = InputHandler.getString("Enter your 4-digit PIN: ");
            try {
                if (bank.withdraw(accountId, amount, pin)) {
                    Account acc = bank.getAccount(accountId);
                    System.out.println("✅ Withdrawal successful! New Balance: ₹" + acc.getBalance());
                }
                return;
            } catch (InvalidTransactionException e) {
                System.out.println("❌ " + e.getMessage());
                attempts++;
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("🔁 Wrong PIN. Try again (" + (MAX_ATTEMPTS - attempts) + " attempts left).");
                } else {
                    System.out.println("🚫 Too many wrong attempts. Transaction cancelled.");
                }
            }
        }
    }
}
