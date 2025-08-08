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

public class UserMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();

    public void show(User user) {
        while (true) {
            try {
                System.out.println("\n--- User Menu ---");
                System.out.println("1. View Menu");
                System.out.println("2. Place Order");
                System.out.println("3. View My Orders");
                System.out.println("4. Cancel My Pending Order");
                System.out.println("5. Logout");

                int choice = InputValidator.getInt(sc, "Enter your choice");

                switch (choice) {
                    case 1 -> viewMenu();
                    case 2 -> placeOrder(user);
                    case 3 -> viewUserOrders(user);
                    case 4 -> cancelPendingOrder(user);
                    case 5 -> {
                        System.out.println("👋 Logged out.");
                        return;
                    }
                    default -> System.out.println("⚠️ Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Something went wrong: " + e.getMessage());
            }
        }
    }

    private void viewMenu() {
        List<FoodItem> foodList = foodService.getAllFoods();
        if (foodList.isEmpty()) {
            System.out.println("📭 Menu is empty.");
        } else {
            TablePrinter.printFoodTable(foodList);
        }
    }

    private void placeOrder(User user) {
        while (true) {
            int foodId = InputValidator.getInt(sc, "Enter Food ID");

            FoodItem food = foodService.getFoodById(foodId);
            if (food == null) {
                System.out.println("❌ Food item not found. Try again.");
                continue;
            }

            int quantity = InputValidator.getInt(sc, "Enter Quantity");
            if (quantity <= 0) {
                System.out.println("❌ Quantity must be positive. Try again.");
                continue;
            }

            double total = food.getPrice() * quantity;
            boolean placed = orderService.placeOrder(user.getId(), foodId, quantity, total);

            if (placed) {
                System.out.println("✅ Order placed! Total: ₹" + total);
            } else {
                System.out.println("❌ Failed to place order.");
            }
            break;
        }
    }

    private void viewUserOrders(User user) {
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        if (orders.isEmpty()) {
            System.out.println("📭 You haven't placed any orders.");
        } else {
            System.out.println("\n--- My Orders ---");
            for (Order o : orders) {
                FoodItem food = foodService.getFoodById(o.getFoodId());
                String foodName = (food != null) ? food.getName() : "Unknown";
                System.out.println("🧾 Order ID: " + o.getId()
                        + " | Food: " + foodName
                        + " | Qty: " + o.getQuantity()
                        + " | Total: ₹" + o.getTotalPrice()
                        + " | Date: " + o.getOrderDate()
                        + " | Status: " + o.getStatus());
            }
        }
    }

    private void cancelPendingOrder(User user) {
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        List<Order> pendingOrders = orders.stream()
                                          .filter(o -> "Pending".equalsIgnoreCase(o.getStatus()))
                                          .toList();

        if (pendingOrders.isEmpty()) {
            System.out.println("⚠️ You have no pending orders to cancel.");
            return;
        }

        System.out.println("\n--- Pending Orders ---");
        pendingOrders.forEach(o -> System.out.println("Order ID: " + o.getId()
                + " | Food ID: " + o.getFoodId()
                + " | Qty: " + o.getQuantity()
                + " | Total: ₹" + o.getTotalPrice()
                + " | Date: " + o.getOrderDate()));

        int orderId = InputValidator.getInt(sc, "Enter Order ID to cancel");
        boolean cancelled = orderService.updateOrderStatus(orderId, "Cancelled");

        if (cancelled) {
            System.out.println("✅ Order cancelled successfully.");
        } else {
            System.out.println("❌ Unable to cancel order. Make sure it's pending.");
        }
    }
}
