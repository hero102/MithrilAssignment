package com.aurionpro.bank.service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.model.DBConnection;
import com.aurionpro.bank.model.Transaction;

public class TransactionService {

    // Save Transaction to DB
    public void recordTransaction(int fromId, int toId, BigDecimal amount, String type) throws SQLException {
        String sql = "INSERT INTO transactions (from_account, to_account, amount, transaction_type) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fromId);
            stmt.setInt(2, toId);
            stmt.setBigDecimal(3, amount);
            stmt.setString(4, type);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Fetch All Transactions
    public List<Transaction> getAllTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<Transaction>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Fetch Transactions for Specific Account
    public List<Transaction> getTransactionsForAccount(int accId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account = ? OR to_account = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<Transaction>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accId);
            stmt.setInt(2, accId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Print Receipt for Deposit / Withdraw
    public void printReceipt(Account acc, String type, BigDecimal amount, BigDecimal newBalance) {
        System.out.println("\n===== " + type + " Receipt =====");
        System.out.println("Account Holder : " + acc.getName());
        System.out.println("Account No     : " + acc.getAccountNumber());
        System.out.println("Transaction    : " + type);
        System.out.println("Amount         : ₹" + amount);
        System.out.println("New Balance    : ₹" + newBalance);
        System.out.println("Date           : " + new java.util.Date());
        System.out.println("===============================");
    }

    // Print Receipt for Transfer
    public void printTransferReceipt(Account sender, Account receiver, BigDecimal amount) {
        System.out.println("\n===== Transfer Receipt =====");
        System.out.println("From Account : " + sender.getAccountNumber() + " (" + sender.getName() + ")");
        System.out.println("To Account   : " + receiver.getAccountNumber() + " (" + receiver.getName() + ")");
        System.out.println("Amount       : ₹" + amount);
        System.out.println("Sender Balance After Transfer   : ₹" + sender.getBalance());
        System.out.println("Receiver Balance After Transfer : ₹" + receiver.getBalance());
        System.out.println("Date         : " + new java.util.Date());
        System.out.println("============================");
    }

    // Helper: Map ResultSet → Transaction
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("from_account"),
                rs.getInt("to_account"),
                rs.getBigDecimal("amount"),
                rs.getTimestamp("transaction_date"),
                rs.getString("transaction_type")
        );
    }
}
