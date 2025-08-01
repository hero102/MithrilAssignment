package com.aurionpro.bank.model;

import java.math.BigDecimal;

public class Account {
    private int accountNumber;
    private String name;
    private String pin;
    private BigDecimal balance;
    private boolean isActive;

    public Account(int accountNumber, String name, BigDecimal balance, String pin, boolean isActive) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.isActive = isActive;
    }

    public Account(String name, String pin, BigDecimal balance) {
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.isActive = true;
    }

    // Getters & Setters
    public int getAccountNumber() { return accountNumber; }
    public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }
    
    public int getId() { 
        return accountNumber; 
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}
