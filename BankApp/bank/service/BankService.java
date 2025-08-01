package com.aurionpro.bank.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.model.AccountDAO;
import com.aurionpro.bank.model.DBConnection;

public class BankService {
    private AccountDAO accountDao = new AccountDAO();

    // Admin: Create Account
    public int createNewAccount(String name, String pin, BigDecimal balance) throws SQLException, InvalidTransactionException {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionException("Initial deposit cannot be negative.");
        }
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new InvalidTransactionException("PIN must be exactly 4 digits.");
        }

        Account acc = new Account(name, pin, balance);
        return accountDao.createAccount(acc);
    }

    // Admin: View Accounts
    public List<Account> viewAccounts(String type) throws SQLException {
        return accountDao.getAccounts(type);
    }

    // Admin: Change Account Status
    public void setAccountStatus(int accNo, boolean status) throws SQLException {
        if (!status) {
            accountDao.setInactive(accNo);
        } else {
            Account acc = accountDao.findById(accNo);
            if (acc != null) {
                acc.setActive(true);
                accountDao.update(acc);
            }
        }
    }

    // User: Login
    public Account loginUser(int accNo, String pin) throws SQLException {
        return accountDao.loginUser(accNo, pin);
    }

    // User: Deposit
    public void deposit(Account acc, BigDecimal amount) throws SQLException, InvalidTransactionException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Deposit amount must be positive.");
        }

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            BigDecimal newBal = acc.getBalance().add(amount);
            acc.setBalance(newBal);
            accountDao.update(acc);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // User: Withdraw
    public void withdraw(Account acc, BigDecimal amount) throws SQLException, InvalidTransactionException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal amount must be positive.");
        }
        if (amount.compareTo(acc.getBalance()) > 0) {
            throw new InvalidTransactionException("Insufficient funds.");
        }

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            BigDecimal newBal = acc.getBalance().subtract(amount);
            acc.setBalance(newBal);
            accountDao.update(acc);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
}
