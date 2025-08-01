package com.aurionpro.bank.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Create Account and return generated ID
    public int createAccount(Account acc) throws SQLException {
        String sql = "INSERT INTO accounts(name, pin, balance, isActive) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, acc.getName());
            stmt.setString(2, acc.getPin());
            stmt.setBigDecimal(3, acc.getBalance());
            stmt.setBoolean(4, true);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Find by ID
    public Account findById(int accNo) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE accountNumber=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accNo);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Login user with PIN
    public Account loginUser(int accNo, String pin) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE accountNumber=? AND pin=? AND isActive=true";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accNo);
            stmt.setString(2, pin);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Get Accounts (all / active / inactive)
    public List<Account> getAccounts(String type) throws SQLException {
        String sql;
        if ("active".equalsIgnoreCase(type)) {
            sql = "SELECT * FROM accounts WHERE isActive=true";
        } else if ("inactive".equalsIgnoreCase(type)) {
            sql = "SELECT * FROM accounts WHERE isActive=false";
        } else {
            sql = "SELECT * FROM accounts";
        }

        List<Account> list = new ArrayList<Account>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(mapResultSetToAccount(rs));
            }
            return list;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Update Account
    public boolean update(Account acc) throws SQLException {
        String sql = "UPDATE accounts SET name=?, pin=?, balance=?, isActive=? WHERE accountNumber=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc.getName());
            stmt.setString(2, acc.getPin());
            stmt.setBigDecimal(3, acc.getBalance());
            stmt.setBoolean(4, acc.isActive());
            stmt.setInt(5, acc.getAccountNumber());
            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Deactivate Account
    public boolean setInactive(int accNo) throws SQLException {
        String sql = "UPDATE accounts SET isActive=false WHERE accountNumber=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accNo);
            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
 // Set Account Active/Inactive
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


    // Helper method to map ResultSet → Account
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("accountNumber"),
                rs.getString("name"),
                rs.getBigDecimal("balance"),
                rs.getString("pin"),
                rs.getBoolean("isActive")
        );
    }
}
