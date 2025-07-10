package com.aurionpro.EcommerceModel;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class GetInput {

    public int getIntInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid integer number.");
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Scanner error or closed.");
                System.exit(1);
            }
        }
    }

    public double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid number.");
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Scanner error or closed.");
                System.exit(1);
            }
        }
    }

    public double getPositiveNumber(Scanner scanner) {
        double number;
        do {
            number = getDoubleInput(scanner);
            if (number < 0)
                System.out.println("Please enter a positive number!");
        } while (number < 0);
        return number;
    }

    public int getPositiveIntNumber(Scanner scanner) {
        int number;
        do {
            number = getIntInput(scanner);
            if (number < 0)
                System.out.println("Please enter a positive integer!");
        } while (number < 0);
        return number;
    }

    public int getChoice(Scanner scanner, String prompt, int max) {
        int choice;
        while (true) {
            System.out.println(prompt);
            choice = getPositiveIntNumber(scanner);
            if (choice >= 1 && choice <= max)
                return choice;
            System.out.println("Invalid choice. Please enter a number between 1 and " + max + ".");
        }
    }
}
