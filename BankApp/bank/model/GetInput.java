package com.aurionpro.bank.model;

import java.util.Scanner;

public class GetInput {

    public int getPositiveAmount(Scanner scanner) {
        while (true) {
            System.out.print("Enter a positive amount: ");
            String input = scanner.nextLine();

            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a whole positive number.");
                continue;
            }

            int amount = Integer.parseInt(input);
            if (amount > 0) {
                return amount;
            } else {
                System.out.println("Amount must be greater than 0. Try again.");
            }
        }
    }

    public int getChoiceInRange(Scanner scanner, int min, int max) {
        while (true) {
            System.out.print("Enter choice (" + min + "-" + max + "): ");
            String input = scanner.nextLine();

            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            int choice = Integer.parseInt(input);
            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("Choice must be between " + min + " and " + max + ".");
            }
        }
    }
}
