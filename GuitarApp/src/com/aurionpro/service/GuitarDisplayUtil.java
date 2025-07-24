package com.aurionpro.service;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.model.Guitar;
import com.aurionpro.model.GuitarPrinter;
import com.aurionpro.model.InputValidator;

public class GuitarDisplayUtil {
    public static void displayGuitars(String message, List<Guitar> guitars) {
        System.out.println("\n" + message);
        GuitarPrinter.printHeader();
        for (int i = 0; i < guitars.size(); i++) {
            System.out.println((i + 1) + ". " + guitars.get(i));
        }
    }

    public static <T> int printMenuAndSelect(Scanner sc, List<T> options, String label) {
        System.out.println(label + ":");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        return InputValidator.getValidatedInt(sc, "Enter choice: ", 1, options.size()) - 1;
    }
}
