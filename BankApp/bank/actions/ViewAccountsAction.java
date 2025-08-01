package com.aurionpro.bank.actions;

import java.sql.SQLException;
import java.util.List;

import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class ViewAccountsAction {
    private final BankFacade bank;

    public ViewAccountsAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        System.out.println("\n--- View Accounts ---");
        System.out.println("1. All Accounts");
        System.out.println("2. Active Accounts");
        System.out.println("3. Inactive Accounts");

        int choice = InputHandler.getInt("Choose an option: ");

        String type = "ALL";
        if (choice == 2) type = "ACTIVE";
        else if (choice == 3) type = "INACTIVE";

        List<Account> accounts = bank.getAccounts(type);
        if (accounts.isEmpty()) {
            System.out.println("⚠️ No accounts found.");
            return;
        }

        System.out.println("\n--- Accounts List ---");
        for (Account acc : accounts) {
            System.out.printf("ID: %-3d | Name: %-15s | Balance: ₹%-10.2f | Status: %s\n",
                    acc.getAccountNumber(),
                    acc.getName(),
                    acc.getBalance(),
                    acc.isActive() ? "Active" : "Inactive");
        }
    }
}
