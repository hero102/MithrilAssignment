package com.aurionpro.util;

import java.util.List;
import com.aurionpro.model.FoodItem;

public class TablePrinter {
    public static void printFoodTable(List<FoodItem> foodList) {
        if (foodList == null || foodList.isEmpty()) {
            System.out.println("No food items found.");
            return;
        }

        String borderTop = "╔══════╦══════════════════════════════════╦════════════╗";
        String borderMid = "╠══════╬══════════════════════════════════╬════════════╣";
        String borderBot = "╚══════╩══════════════════════════════════╩════════════╝";
        String rowFormat = "║ %-4d ║ %-32s ║ ₹%-9.2f ║\n";

        System.out.println(borderTop);
        System.out.printf("║ %-4s ║ %-32s ║ %-10s ║\n", "ID", "Food Name", "Price");
        System.out.println(borderMid);

        for (FoodItem f : foodList) {
            System.out.printf(rowFormat, f.getId(), f.getName(), f.getPrice());
        }

        System.out.println(borderBot);
    }
}
