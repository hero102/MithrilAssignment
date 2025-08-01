package com.aurionpro.bank.service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.bank.model.Transaction;

public class TransactionDAO {

    // ✅ For Transfer (Two Accounts)
    public void addTransaction(int fromAccount, int toAccount, BigDecimal amount, String type, Connection conn) throws SQLException {
        String sql = "INSERT INTO transactions (from_account, to_account, amount, transaction_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromAccount);
            stmt.setInt(2, toAccount);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, type);
            stmt.executeUpdate();
        }
    }

    // ✅ For Deposit/Withdraw (One Account only)
    public void addTransactionSingleAccount(int accId, BigDecimal amount, String type, Connection conn) throws SQLException {
        String sql = "INSERT INTO transactions (from_account, to_account, amount, transaction_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (type.equalsIgnoreCase("DEPOSIT")) {
                stmt.setNull(1, java.sql.Types.INTEGER); // from = NULL
                stmt.setInt(2, accId); // to = account
            } else if (type.equalsIgnoreCase("WITHDRAW")) {
                stmt.setInt(1, accId); // from = account
                stmt.setNull(2, java.sql.Types.INTEGER); // to = NULL
            }
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, type);
            stmt.executeUpdate();
        }
    }

    // Get all transactions
    public List<Transaction> getAllTransactions(Connection conn) throws SQLException {
        String sql = "SELECT id, from_account, to_account, amount, transaction_date, transaction_type " +
                     "FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapTransaction(rs));
            }
        }
        return transactions;
    }

    // Get transactions for a specific account
    public List<Transaction> getTransactionsForAccount(int accId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account=? OR to_account=? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accId);
            stmt.setInt(2, accId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapTransaction(rs));
                }
            }
        }
        return transactions;
    }

    // Utility
    private Transaction mapTransaction(ResultSet rs) throws SQLException {
        int fromAcc = rs.getObject("from_account") == null ? 0 : rs.getInt("from_account");
        int toAcc = rs.getObject("to_account") == null ? 0 : rs.getInt("to_account");

        return new Transaction(
            rs.getInt("id"),
            fromAcc,
            toAcc,
            rs.getBigDecimal("amount"),
            rs.getTimestamp("transaction_date"),
            rs.getString("transaction_type")
        );
    }
}
