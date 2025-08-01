package com.aurionpro.bank.model;


import java.math.BigDecimal;
import java.util.Scanner;

public class InputUtil {
    public static int getInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Enter valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static BigDecimal getPositiveDecimal(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextBigDecimal()) {
            System.out.print("Enter a valid amount: ");
            sc.next();
        }
        BigDecimal amt = sc.nextBigDecimal();
        if (amt.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be positive.");
            return getPositiveDecimal(sc, prompt);
        }
        return amt;
    }

    public static String getString(Scanner sc, String prompt) {
        System.out.print(prompt);
        sc.nextLine();
        return sc.nextLine();
    }
}

