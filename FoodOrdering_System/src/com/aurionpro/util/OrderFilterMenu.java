package com.aurionpro.util;

import java.util.Scanner;

import com.aurionpro.service.OrderService;

public class OrderFilterMenu {
    private final Scanner sc = new Scanner(System.in);
    private final OrderService orderService;

    public OrderFilterMenu(OrderService orderService) {
        this.orderService = orderService;
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Filter Orders ---");
            System.out.println("1. View Pending Orders");
            System.out.println("2. View Completed Orders");
            System.out.println("3. View Cancelled Orders");
            System.out.println("4. Back");

            int choice = InputValidator.getInt(sc, "Enter choice: ");

            switch (choice) {
                case 1 -> orderService.viewOrdersByStatus("Pending");
                case 2 -> orderService.viewOrdersByStatus("Completed");
                case 3 -> orderService.viewOrdersByStatus("Cancelled");
                case 4 -> {
                    return; // Back to previous menu
                }
                default -> System.out.println("⚠ Invalid choice.");
            }
        }
    }
}
