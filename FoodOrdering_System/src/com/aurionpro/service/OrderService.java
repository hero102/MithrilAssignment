package com.aurionpro.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.aurionpro.dao.OrderDAO;
import com.aurionpro.model.Order;
import com.aurionpro.util.DBConnection;

public class OrderService {
    private OrderDAO orderDAO = new OrderDAO();

    public boolean placeOrder(int userId, int foodId, int quantity, double totalPrice) {
        Order order = new Order(0, userId, null, foodId, quantity, totalPrice, new Timestamp(System.currentTimeMillis()), "Pending");
        return orderDAO.placeOrder(order);
    }

    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.getOrdersByUser(userId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void viewOrdersByUser(String username) {
        List<Order> orders = orderDAO.getOrdersByUsername(username);
        if (orders.isEmpty()) {
            System.out.println("⚠ No orders found.");
            return;
        }

        System.out.println("+----+----------------+----------+----------+------------+----------------+");
        System.out.println("| ID | Food Item      | Quantity | Price    | Total      | Status         |");
        System.out.println("+----+----------------+----------+----------+------------+----------------+");
        for (Order order : orders) {
            System.out.printf("| %-2d | %-14s | %-8d | %-8.2f | %-10.2f | %-14s |\n",
                    order.getId(), order.getFoodName(), order.getQuantity(),
                    order.getPrice(), order.getTotalPrice(), order.getStatus());
        }
        System.out.println("+----+----------------+----------+----------+------------+----------------+");
    }

    // Filter orders by status
    public void viewOrdersByStatus(String status) {
        List<Order> orders = orderDAO.getOrdersByStatus(status);
        if (orders.isEmpty()) {
            System.out.println("⚠ No orders with status: " + status);
            return;
        }

        for (Order order : orders) {
            System.out.println(order);
        }
    }

 // Cancel an order (user can cancel only their own order)
    public boolean cancelOrder(int userId, int orderId) {
        Order order = orderDAO.getOrderById(orderId);

        // Check if order exists and belongs to the user
        if (order == null) {
            System.out.println("❌ Order not found.");
            return false;
        }
        if (order.getUserId() != userId) {
            System.out.println("⛔ You are not authorized to cancel this order.");
            return false;
        }
        if (order.getStatus().equalsIgnoreCase("Cancelled")) {
            System.out.println("⚠ Order is already cancelled.");
            return false;
        }
        if (order.getStatus().equalsIgnoreCase("Successful")) {
            System.out.println("⚠ Cannot cancel a successful order.");
            return false;
        }

        boolean cancelled = orderDAO.updateOrderStatus(orderId, "Cancelled");
        return cancelled;
    }
    public boolean isFoodIdExists(int foodId) {
        return orderDAO.doesFoodExist(foodId); // Make sure this DAO method checks existence
    }

    public boolean isUserIdExists(int userId) {
        return orderDAO.doesUserExist(userId); // You can check from UserDAO if separate
    }


    // Paginated order view (for Admin)
    public void viewOrdersPaginated(int page, int pageSize) {
        List<Order> orders = orderDAO.getOrdersPaginated(page, pageSize);
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}
