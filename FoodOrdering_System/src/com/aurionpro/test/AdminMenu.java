package com.aurionpro.test;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.model.FoodItem;
import com.aurionpro.model.Order;
import com.aurionpro.model.User;
import com.aurionpro.service.FoodService;
import com.aurionpro.service.OrderService;
import com.aurionpro.util.InputValidator;
import com.aurionpro.util.TablePrinter;

public class AdminMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();

    public void show(User user) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View Food Items");
            System.out.println("2. Add Food Item");
            System.out.println("3. Update Food Item");
            System.out.println("4. Delete Food Item");
            System.out.println("5. View / Manage Orders");
            System.out.println("6. Logout");

            int choice = InputValidator.getInt(sc, "Enter your choice: ");

            switch (choice) {
                case 1 -> viewFoodItems();
                case 2 -> addFoodItem();
                case 3 -> updateFoodItem();
                case 4 -> deleteFoodItem();
                case 5 -> manageOrders();
                case 6 -> {
                    System.out.println("👋 Logged out.");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }

    private void viewFoodItems() {
        List<FoodItem> foodList = foodService.getAllFoods();
        if (foodList.isEmpty()) {
            System.out.println("⚠️ No food items found.");
        } else {
            TablePrinter.printFoodTable(foodList);
        }
    }

    private void addFoodItem() {
        System.out.print("Food Name: ");
        String name = sc.nextLine().trim();

        double price = InputValidator.getDouble(sc, "Price: ");
        foodService.addFood(name, price);

        System.out.println("✅ Food item added.");
    }

    private void updateFoodItem() {
        int id = InputValidator.getInt(sc, "Food ID to Update: ");

        FoodItem food = foodService.getFoodById(id);
        if (food == null) {
            System.out.println("❌ Food item not found.");
            return;
        }

        System.out.print("New Name: ");
        String newName = sc.nextLine().trim();

        double newPrice = InputValidator.getDouble(sc, "New Price: ");
        foodService.updateFood(id, newName, newPrice);

        System.out.println("✅ Food item updated.");
    }

    private void deleteFoodItem() {
        int id = InputValidator.getInt(sc, "Food ID to Delete: ");

        FoodItem food = foodService.getFoodById(id);
        if (food == null) {
            System.out.println("❌ Food item not found.");
            return;
        }

        foodService.deleteFood(id);
        System.out.println("✅ Food item deleted.");
    }

    private void manageOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("⚠️ No orders found.");
            return;
        }

        System.out.println("\n--- Orders ---");
        for (Order o : orders) {
            System.out.println(o);
        }

        System.out.println("\n--- Order Management ---");
        System.out.println("1. Mark Order as Completed");
        System.out.println("2. Delete Order");
        System.out.println("3. Filter Orders");
        System.out.println("4. Back to Menu");

        int choice = InputValidator.getInt(sc, "Enter your choice: ");

        switch (choice) {
            case 1 -> {
                int orderId = InputValidator.getInt(sc, "Enter Order ID to mark as Completed: ");
                if (orderService.updateOrderStatus(orderId, "Completed")) {
                    System.out.println("✅ Order marked as Completed.");
                } else {
                    System.out.println("❌ Order not found.");
                }
            }
            case 2 -> {
                int orderId = InputValidator.getInt(sc, "Enter Order ID to Delete: ");
                if (orderService.deleteOrder(orderId)) {
                    System.out.println("✅ Order deleted.");
                } else {
                    System.out.println("❌ Order not found.");
                }
            }
            case 3 -> {
                new com.aurionpro.util.OrderFilterMenu(orderService).show();
            }
            case 4 -> System.out.println("↩ Returning to menu...");
            default -> System.out.println("⚠️ Invalid choice.");
        }
    }

}
