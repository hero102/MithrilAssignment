package com.aurionpro.service;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.model.Builder;
import com.aurionpro.model.Guitar;
import com.aurionpro.model.GuitarPrinter;
import com.aurionpro.model.InputValidator;
import com.aurionpro.model.Type;
import com.aurionpro.model.Wood;



public class GuitarSearchService {
    private final Inventory inventory;
    private final Scanner sc;

    public GuitarSearchService(Inventory inventory, Scanner sc) {
        this.inventory = inventory;
        this.sc = sc;
    }

    public void startSearch() {
        System.out.println("\n SEARCH GUITAR ");
        List<Guitar> results = inventory.getAllGuitars();

        if (results.isEmpty()) {
            System.out.println(" No guitars available.");
            return;
        }

        displayGuitars(" All Guitars:", results);

        while (true) {
            System.out.println("\nDo you want to filter?");
            System.out.println("1. Yes\n2. No");
            int choice = InputValidator.getValidatedInt(sc, "Enter choice: ", 1, 2);
            if (choice == 2) break;

            results = applyDynamicFilter(results);
            if (results.isEmpty()) {
                System.out.println(" No guitars match this filter.");
                return;
            }

            displayGuitars(" Filtered Guitars:", results);
        }
    }

    private List<Guitar> applyDynamicFilter(List<Guitar> list) {
        System.out.println("Filter by:");
        System.out.println("1. Model\n2. Type\n3. Builder\n4. Back Wood\n5. Top Wood");

        int filterChoice = InputValidator.getValidatedInt(sc, "Select filter: ", 1, 5);

        switch (filterChoice) {
            case 1 -> {
                List<String> models = list.stream()
                        .map(g -> g.getSpec().getModel())
                        .distinct()
                        .sorted()
                        .toList();

                if (models.isEmpty()) return Collections.emptyList();

                int choice = printMenuAndSelect(models, "Select Model");
                return list.stream()
                        .filter(g -> g.getSpec().getModel().equalsIgnoreCase(models.get(choice)))
                        .toList();
            }

            case 2 -> {
                List<Type> types = list.stream()
                        .map(g -> g.getSpec().getType())
                        .distinct()
                        .toList();

                if (types.isEmpty()) return Collections.emptyList();

                int choice = printMenuAndSelect(types, "Select Type");
                return list.stream()
                        .filter(g -> g.getSpec().getType() == types.get(choice))
                        .toList();
            }

            case 3 -> {
                List<Builder> builders = list.stream()
                        .map(g -> g.getSpec().getBuilder())
                        .distinct()
                        .toList();

                if (builders.isEmpty()) return Collections.emptyList();

                int choice = printMenuAndSelect(builders, "Select Builder");
                return list.stream()
                        .filter(g -> g.getSpec().getBuilder() == builders.get(choice))
                        .toList();
            }

            case 4 -> {
                List<Wood> backWoods = list.stream()
                        .map(g -> g.getSpec().getBackWood())
                        .distinct()
                        .toList();

                if (backWoods.isEmpty()) return Collections.emptyList();

                int choice = printMenuAndSelect(backWoods, "Select Back Wood");
                return list.stream()
                        .filter(g -> g.getSpec().getBackWood() == backWoods.get(choice))
                        .toList();
            }

            case 5 -> {
                List<Wood> topWoods = list.stream()
                        .map(g -> g.getSpec().getTopWood())
                        .distinct()
                        .toList();

                if (topWoods.isEmpty()) return Collections.emptyList();

                int choice = printMenuAndSelect(topWoods, "Select Top Wood");
                return list.stream()
                        .filter(g -> g.getSpec().getTopWood() == topWoods.get(choice))
                        .toList();
            }

            default -> {
                System.out.println("Invalid filter option.");
                return list;
            }
        }
    }

    // Generic menu printer for enums and strings
    private <T> int printMenuAndSelect(List<T> options, String label) {
        System.out.println(label + ":");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        return InputValidator.getValidatedInt(sc, "Enter choice: ", 1, options.size()) - 1;
    }

    private void displayGuitars(String message, List<Guitar> guitars) {
        System.out.println("\n" + message);
        GuitarPrinter.printHeader();
        for (int i = 0; i < guitars.size(); i++) {
            System.out.println((i + 1) + ". " + guitars.get(i));
        }
    }
}
