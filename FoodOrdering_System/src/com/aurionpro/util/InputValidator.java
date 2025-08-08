package com.aurionpro.util;

import java.util.Scanner;

import com.aurionpro.service.FoodService;
import com.aurionpro.service.OrderService;
import com.aurionpro.service.UserService;

public class InputValidator {
    private static Scanner sc = new Scanner(System.in);
    private static FoodService foodService = new FoodService();
    private static UserService userService = new UserService();
    private static OrderService orderService = new OrderService();

    // Validate integer input
    public static int getValidatedInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please try again.");
            }
        }
    }

    // Validate double input
    public static double getValidatedDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid decimal number. Please try again.");
            }
        }
    }

    public static int getInt(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
            }
        }
    }

    public static double getDouble(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid price.");
            }
        }
    }
    // Check if food exists
    public static boolean foodExists(int foodId) {
        return foodService.getFoodById(foodId) != null;
    }

    // Check if user exists
    public static boolean userExists(int userId) {
        return userService.getUserById(userId) != null;
    }

    // Check if an order exists for a specific user
    public static boolean orderExistsForUser(int userId, int orderId) {
        return orderService.getOrdersByUser(userId)
                .stream()
                .anyMatch(order -> order.getId() == orderId);
    }

    // Prompt for yes/no confirmation
    public static boolean confirmPrompt(String prompt) {
        System.out.print(prompt + " (yes/no): ");
        String input = sc.nextLine().trim();
        return input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y");
    }

    // Validate input as a non-empty string
    public static String getValidatedString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("❌ Input cannot be empty.");
        }
    }
}
