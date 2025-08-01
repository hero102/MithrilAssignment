package com.aurionpro.bank.actions;

import java.sql.SQLException;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;

public class CloseAccountAction {
    private final BankFacade bank;

    public CloseAccountAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int id = InputHandler.getInt("Enter account ID to close: ");
        boolean closed = bank.closeAccount(id);
        if (closed) {
            System.out.println("✅ Account closed successfully (marked as INACTIVE).");
        } else {
            System.out.println("❌ Failed to close account. It may not exist or is already inactive.");
        }
    }
}
