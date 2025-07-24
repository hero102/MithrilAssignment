package com.aurionpro.service;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.model.Guitar;
import com.aurionpro.model.InputValidator;

public class GuitarSearchService {
    private final Inventory inventory;
    private final Scanner sc;
    private final GuitarFilterService filterService;

    public GuitarSearchService(Inventory inventory, Scanner sc) {
        this.inventory = inventory;
        this.sc = sc;
        this.filterService = new GuitarFilterService(sc);
    }

    public void startSearch() {
        System.out.println("\n SEARCH GUITAR ");
        List<Guitar> results = inventory.getAllGuitars();

        if (results.isEmpty()) {
            System.out.println(" No guitars available.");
            return;
        }

        GuitarDisplayUtil.displayGuitars(" All Guitars:", results);

        while (true) {
            System.out.println("\nDo you want to filter?");
            System.out.println("1. Yes\n2. No");
            int choice = InputValidator.getValidatedInt(sc, "Enter choice: ", 1, 2);
            if (choice == 2) break;

            results = filterService.applyDynamicFilter(results);
            if (results.isEmpty()) {
                System.out.println(" No guitars match this filter.");
                return;
            }

            GuitarDisplayUtil.displayGuitars(" Filtered Guitars:", results);
        }
    }
}

