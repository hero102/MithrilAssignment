package com.aurionpro.service;

import java.util.List;

import com.aurionpro.dao.FoodDAO;
import com.aurionpro.model.FoodItem;

public class FoodService {
    private FoodDAO foodDAO = new FoodDAO();

    public boolean addFood(String name, double price) {
        FoodItem item = new FoodItem(0, name, price);
        return foodDAO.addFood(item);
    }

    public boolean updateFood(int id, String name, double price) {
        FoodItem item = new FoodItem(id, name, price);
        return foodDAO.updateFood(item);
    }

    public boolean deleteFood(int id) {
        return foodDAO.deleteFood(id);
    }

    public List<FoodItem> getAllFoods() {
        return foodDAO.getAllFoods();
    }

    public FoodItem getFoodById(int id) {
        return foodDAO.getFoodById(id);
    }
}

