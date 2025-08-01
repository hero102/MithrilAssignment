package com.aurionpro.bank.actions;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class TransferFundsAction {
    private final BankFacade bank;

    public TransferFundsAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int fromId = InputHandler.getInt("Enter sender Account ID: ");
        int toId = InputHandler.getInt("Enter receiver Account ID: ");
        BigDecimal amount = InputHandler.getAmount("Enter transfer amount (₹): ");
        String pin = InputHandler.getString("Enter your 4-digit PIN: ");

        try {
            if (bank.transfer(fromId, toId, amount, pin)) {
                Account sender = bank.getAccount(fromId);
                Account receiver = bank.getAccount(toId);
                System.out.println("✅ Transfer successful!");
                System.out.println("Sender Balance: ₹" + sender.getBalance());
                System.out.println("Receiver Balance: ₹" + receiver.getBalance());
            }
        } catch (InvalidTransactionException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}
