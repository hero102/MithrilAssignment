package com.aurionpro.bank.actions;

import java.sql.SQLException;

import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class SetAccountStatusAction {
    private final BankFacade bank;

    public SetAccountStatusAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int accId = InputHandler.getInt("Enter Account ID: ");
        int statusInput = InputHandler.getInt("Enter 1 to Activate, 0 to Deactivate: ");
        boolean status = (statusInput == 1);

        if (bank.setAccountStatus(accId, status)) {
            System.out.println("✅ Account status updated successfully!");
        } else {
            System.out.println("❌ Failed to update account status.");
        }
    }
}
