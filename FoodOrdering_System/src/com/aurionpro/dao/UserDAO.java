package com.aurionpro.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.model.User;
import com.aurionpro.util.DBConnection;

public class UserDAO {
	
	
	 public boolean register(User user) throws SQLException {
	        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, user.getUsername());
	            stmt.setString(2, user.getPassword());
	            stmt.setString(3,"user");

	            int rowsInserted = stmt.executeUpdate();
	            return rowsInserted > 0;

	        } catch (SQLException e) {
	            // Duplicate username error
	            if (e.getMessage().contains("Duplicate entry")) {
	                System.out.println("❌ Username already exists. Please try a different one.");
	                return false;
	            }
	            throw e; // propagate other exceptions
	        }
	    }
	
	 public User login(String username, String password) {
		    try (Connection conn = DBConnection.getConnection()) {
		        String sql = "SELECT * FROM users WHERE username=? AND password=?";
		        PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, username);
		        ps.setString(2, password);
		        ResultSet rs = ps.executeQuery();
		        if (rs.next()) {
		            return new User(
		                rs.getInt("id"),                   // ✅ Pass ID
		                rs.getString("username"),
		                rs.getString("password")
		            );
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return null;
		}
         
 public boolean register(String username, String password) {
     try (Connection conn = DBConnection.getConnection()) {
         String sql = "INSERT INTO users(username,password,role) VALUES(?,?,?)";
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, username);
         ps.setString(2, password);
         ps.setString(3, "user");
         return ps.executeUpdate() > 0;
     } catch (Exception e) { e.printStackTrace(); }
     return false;
 }
 public User getUserById(int id) {
	    User user = null;
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            user = new User(
	                rs.getInt("id"),
	                rs.getString("username"),
	                rs.getString("password")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return user;
	}

}

