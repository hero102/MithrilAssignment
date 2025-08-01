package com.aurionpro.bank.actions;

import java.sql.SQLException;
import java.util.List;

import com.aurionpro.bank.model.Transaction;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;
import com.aurionpro.bank.util.TransactionPrinter;

public class TransactionHistoryAction {
    private final BankFacade bank;

    public TransactionHistoryAction(BankFacade bank) {
        this.bank = bank;
    }

    public void execute() throws SQLException {
        int choice = InputHandler.getInt("\n1. All Transactions | 2. Specific Account: ");
        if (choice == 1) {
            List<Transaction> transactions = bank.getAllTransactions();
            TransactionPrinter.printTransactions(transactions);
        } else if (choice == 2) {
            int accId = InputHandler.getInt("Enter Account ID: ");
            List<Transaction> transactions = bank.getTransactionsForAccount(accId);
            TransactionPrinter.printTransactions(transactions);
        } else {
            System.out.println("❌ Invalid choice.");
        }
    }
}
