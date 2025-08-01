package com.aurionpro.bank.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.service.BankFacade;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    // 🔹 Get Integer (with validation)
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please enter a valid integer.");
            }
        }
    }

    // 🔹 Get Positive Integer (e.g. for deposit/withdraw amount)
    public static int getPositiveInt(String prompt) {
        while (true) {
            int value = getInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("❌ Enter a number greater than 0.");
        }
    }

    // 🔹 Get BigDecimal Amount (used when precision is required)
    public static BigDecimal getAmount(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
                System.out.println("❌ Amount must be greater than 0.");
            } catch (Exception e) {
                System.out.println("❌ Invalid amount. Enter a valid number.");
            }
        }
    }

    // 🔹 Get Non-Empty String
    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ Input cannot be empty.");
        }
    }

    // 🔹 Get PIN (must be 4 digits)
    public static String getPin(String prompt) {
        while (true) {
            System.out.print(prompt);
            String pin = scanner.nextLine().trim();
            if (pin.matches("\\d{4}")) {
                return pin;
            }
            System.out.println("❌ PIN must be exactly 4 digits.");
        }
    }

    // 🔹 Valid Account ID input (loops until valid ID & active account)
    public static int getValidAccountId(String prompt, BankFacade bank) throws SQLException {
        while (true) {
            int id = getInt(prompt);
            Account acc = bank.getAccount(id);

            if (acc != null && acc.isActive()) {
                return id; // valid account मिला
            }
            System.out.println("❌ Invalid or inactive Account ID. Please try again.");
        }
    }

    // 🔹 Valid Sender and Receiver IDs for Transfer
    public static int[] getValidTransferIds(BankFacade bank) throws SQLException {
        int senderId, receiverId;
        while (true) {
            senderId = getValidAccountId("Enter Sender Account ID: ", bank);
            receiverId = getValidAccountId("Enter Receiver Account ID: ", bank);

            if (senderId == receiverId) {
                System.out.println("❌ Sender and Receiver cannot be the same. Please enter different IDs.");
            } else {
                break;
            }
        }
        return new int[]{senderId, receiverId};
    }
}
