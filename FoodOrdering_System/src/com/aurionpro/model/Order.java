package com.aurionpro.model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private String username;
    private int foodId;
    private int quantity;
    private double price;
    private double totalPrice;
    private Timestamp orderDate;
    private String status;
    private String foodName;

    // Constructor for full details (with username and foodName)
    public Order(int id, int userId, String username, int foodId, String foodName,
                 int quantity, double totalPrice, Timestamp orderDate, String status) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Constructor with username but without foodName
    public Order(int id, int userId, String username, int foodId, int quantity,
                 double totalPrice, Timestamp orderDate, String status) {
        this(id, userId, username, foodId, null, quantity, totalPrice, orderDate, status);
    }

    // Constructor without username and foodName
    public Order(int id, int userId, int foodId, int quantity,
                 double totalPrice, Timestamp orderDate, String status) {
        this(id, userId, null, foodId, null, quantity, totalPrice, orderDate, status);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getFoodId() {
        return foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format(
            "Order ID: %d | User: %s (ID: %d) | Food: %s (ID: %d) | Qty: %d | Total: ₹%.2f | Date: %s | Status: %s",
            id,
            username != null ? username : "N/A",
            userId,
            foodName != null ? foodName : "N/A",
            foodId,
            quantity,
            totalPrice,
            orderDate != null ? orderDate.toString() : "N/A",
            status
        );
    }
}
