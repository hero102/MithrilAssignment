package com.aurionpro.bank.actions;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class CreateAccountAction {
    private final BankFacade bank;

    public CreateAccountAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() {
        try {
            String name = InputHandler.getString("Enter account holder name: ");
            String pin = InputHandler.getString("Set a 4-digit PIN: ");
            BigDecimal initialDeposit = InputHandler.getAmount("Enter initial deposit: ");

            if (pin.length() != 4 || !pin.matches("\\d{4}")) {
                System.out.println("❌ PIN must be exactly 4 digits.");
                return;
            }

            int id = bank.createNewAccount(name, pin, initialDeposit);
            if (id > 0) {
                System.out.println("✅ Account created successfully! Account ID: " + id);
            } else {
                System.out.println("❌ Account creation failed.");
            }
        } catch (InvalidTransactionException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        }
    }
}
