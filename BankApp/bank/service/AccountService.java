package com.aurionpro.bank.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.model.AccountDAO;
import com.aurionpro.bank.model.DBConnection;

public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // Create Account
    public int createAccount(String name, String pin, BigDecimal initialDeposit)
            throws SQLException, InvalidTransactionException {
        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException("Initial deposit must be non-negative.");
        }
        if (!pin.matches("\\d{4}")) {
            throw new InvalidTransactionException("PIN must be 4 digits.");
        }

        Account acc = new Account(name, pin, initialDeposit);
        return accountDAO.createAccount(acc);
    }

    // Login
    public Account login(int accNo, String pin) throws SQLException {
        return accountDAO.loginUser(accNo, pin);
    }

    // Get Accounts (Admin)
    public List<Account> getAccounts(String type) throws SQLException {
        return accountDAO.getAccounts(type);
    }

    // Deposit
    public boolean deposit(int accNo, BigDecimal amount, String pin)
            throws SQLException, InvalidTransactionException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Deposit amount must be positive.");
        }

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            Account acc = accountDAO.findById(accNo);
            if (acc == null) throw new InvalidTransactionException("Account not found.");
            if (!acc.getPin().equals(pin)) throw new InvalidTransactionException("Invalid PIN.");

            BigDecimal newBalance = acc.getBalance().add(amount);
            acc.setBalance(newBalance);
            accountDAO.update(acc);
            transactionDAO.addTransaction(accNo, accNo, amount, "DEPOSIT", conn);

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // Withdraw
    public boolean withdraw(int accNo, BigDecimal amount, String pin)
            throws SQLException, InvalidTransactionException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal amount must be positive.");
        }

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            Account acc = accountDAO.findById(accNo);
            if (acc == null) throw new InvalidTransactionException("Account not found.");
            if (!acc.getPin().equals(pin)) throw new InvalidTransactionException("Invalid PIN.");
            if (acc.getBalance().compareTo(amount) < 0)
                throw new InvalidTransactionException("Insufficient balance.");

            BigDecimal newBalance = acc.getBalance().subtract(amount);
            acc.setBalance(newBalance);
            accountDAO.update(acc);
            transactionDAO.addTransaction(accNo, accNo, amount, "WITHDRAW", conn);

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // ✅ Transfer
    public boolean transfer(int fromId, int toId, BigDecimal amount, String pin)
            throws SQLException, InvalidTransactionException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transfer amount must be positive.");
        }

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            Account sender = accountDAO.findById(fromId);
            Account receiver = accountDAO.findById(toId);

            if (sender == null || receiver == null)
                throw new InvalidTransactionException("Invalid account(s).");
            if (!sender.getPin().equals(pin))
                throw new InvalidTransactionException("Invalid PIN.");
            if (sender.getBalance().compareTo(amount) < 0)
                throw new InvalidTransactionException("Insufficient balance.");

            // Update balances
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));

            accountDAO.update(sender);
            accountDAO.update(receiver);

            transactionDAO.addTransaction(fromId, toId, amount, "TRANSFER", conn);

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // ✅ Update PIN
    public boolean updatePin(int accNo, String oldPin, String newPin)
            throws SQLException, InvalidTransactionException {
        if (!newPin.matches("\\d{4}")) {
            throw new InvalidTransactionException("New PIN must be 4 digits.");
        }

        Account acc = accountDAO.findById(accNo);
        if (acc == null) {
            throw new InvalidTransactionException("Account not found.");
        }
        if (!acc.getPin().equals(oldPin)) {
            throw new InvalidTransactionException("Incorrect old PIN.");
        }

        acc.setPin(newPin);
        return accountDAO.update(acc);
    }

    // Close Account
    public boolean closeAccount(int accNo) throws SQLException {
        return accountDAO.setInactive(accNo);
    }

    // Get Account by ID
    public Account getAccount(int accNo) throws SQLException {
        return accountDAO.findById(accNo);
    }

 // Deactivate Account (old method)
    public boolean setInactive(int accNo) throws SQLException {
        return setAccountStatus(accNo, false);
    }

    // New method for both active/inactive
    public boolean setAccountStatus(int accNo, boolean status) throws SQLException {
        String sql = "UPDATE accounts SET isActive=? WHERE accountNumber=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, status);
            stmt.setInt(2, accNo);
            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

}
