package com.aurionpro.bank.actions;

import java.sql.SQLException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class CheckBalanceAction {
    private final BankFacade bank;

    public CheckBalanceAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int id = InputHandler.getInt("Enter account ID: ");
        Account acc = bank.getAccount(id);
        if (acc != null && acc.isActive()) {
            System.out.println("💰 Current Balance: ₹" + acc.getBalance());
        } else {
            System.out.println("❌ Invalid or inactive Account ID.");
        }
    }
}
