package com.aurionpro.bank.test;

import java.sql.SQLException;
import com.aurionpro.bank.service.BankFacade;

public class BankApp {
    public static void main(String[] args) throws SQLException {
        BankFacade bank = new BankFacade();
        MenuHandler menuHandler = new MenuHandler(bank);
        menuHandler.start();
    }
}
