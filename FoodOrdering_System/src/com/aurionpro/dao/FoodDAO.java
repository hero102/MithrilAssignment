package com.aurionpro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.FoodItem;
import com.aurionpro.util.DBConnection;

public class FoodDAO {

    public boolean addFood(FoodItem food) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO food_items(name, price) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, food.getName());
            ps.setDouble(2, food.getPrice());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFood(FoodItem food) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE food_items SET name = ?, price = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, food.getName());
            ps.setDouble(2, food.getPrice());
            ps.setInt(3, food.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFood(int foodId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM food_items WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, foodId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FoodItem> getAllFoods() {
        List<FoodItem> foodList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM food_items";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                foodList.add(new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodList;
    }

    public FoodItem getFoodById(int foodId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM food_items WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new FoodItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
