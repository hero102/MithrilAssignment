package com.aurionpro.service;


import java.sql.SQLException;

import com.aurionpro.dao.UserDAO;
import com.aurionpro.model.User;

public class UserService {
 private UserDAO userDAO = new UserDAO();

 public User login(String username, String password) {
     return userDAO.login(username, password);
 }
 
 public boolean register(User user) {
     try {
         return userDAO.register(user);
     } catch (SQLException e) {
         System.out.println("❌ Registration failed due to a system error.");
         e.printStackTrace();
         return false;
     }
 }

 public boolean register(String username, String password) {
     return userDAO.register(username, password);
 }
 
 public User getUserById(int id) {
	    return userDAO.getUserById(id);
	}

}
