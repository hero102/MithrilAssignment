package com.aurionpro.bank.util;

import java.util.List;
import com.aurionpro.bank.model.Transaction;

public class TransactionPrinter {

    public static void printTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("⚠️ No transactions found.");
            return;
        }

        // Table Header
        System.out.println("\n┌─────┬────────────┬────────────┬────────────┬────────────┬────────────────────────┐");
        System.out.printf("│ %-3s │ %-10s │ %-10s │ %-10s │ %-10s │ %-22s │%n",
                "ID", "From", "To", "Amount(₹)", "Type", "Date");
        System.out.println("├─────┼────────────┼────────────┼────────────┼────────────┼────────────────────────┤");

        // Table Rows
        for (Transaction tx : transactions) {
            String fromAcc = (tx.getFromId() == 0) ? "BANK" : String.valueOf(tx.getFromId());
            String toAcc = (tx.getToId() == 0) ? "BANK" : String.valueOf(tx.getToId());

            System.out.printf("│ %-3d │ %-10s │ %-10s │ %-10.2f │ %-10s │ %-22s │%n",
                    tx.getId(),
                    fromAcc,
                    toAcc,
                    tx.getAmount(),
                    tx.getType(),
                    tx.getDate().toString().substring(0, Math.min(22, tx.getDate().toString().length())));
        }

        // Footer
        System.out.println("└─────┴────────────┴────────────┴────────────┴────────────┴────────────────────────┘");
    }
}
