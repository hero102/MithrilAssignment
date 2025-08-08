package com.aurionpro.model;

public class User {
    private int id;
    private String username;
    private String password;

    // Constructor with ID
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Constructor without ID (e.g., for registration input)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

 
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



    // Setters (optional - depending on use case)
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // No setter for role to prevent overriding the fixed value

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + "]";
    }
}
