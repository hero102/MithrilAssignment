package com.aurionpro.bank.actions;


import java.sql.SQLException;
import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class UpdatePinAction {
    private final BankFacade bank;

    public UpdatePinAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int id = InputHandler.getInt("Enter your Account ID: ");
        String oldPin = InputHandler.getString("Enter your current PIN: ");
        String newPin = InputHandler.getString("Enter new 4-digit PIN: ");

        try {
            boolean updated = bank.updatePin(id, oldPin, newPin);
            if (updated) {
                System.out.println("✅ PIN updated successfully.");
            } else {
                System.out.println("❌ Failed to update PIN. Check details and try again.");
            }
        } catch (InvalidTransactionException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}
