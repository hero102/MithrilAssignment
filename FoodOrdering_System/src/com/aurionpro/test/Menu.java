package com.aurionpro.test;

import java.util.Scanner;

import com.aurionpro.model.User;
import com.aurionpro.service.UserService;

public class Menu {
    private static Scanner sc = new Scanner(System.in);
    private static UserService userService = new UserService();

    public static void start() {
        System.out.println("Welcome to Food Ordering System");
        System.out.println("1. Login\n2. Register");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) login();
        else if (choice == 2) register();
        else System.out.println("Invalid choice");
    }

    private static void login() {
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User user = userService.login(uname, pass);
        if (user != null) {
            if (uname.equals("admin")&& pass.equals("admin123")) {
                new AdminMenu().show(user);
            } else {
                new UserMenu().show(user);
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    public static void register() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user = new User(username, password);

        boolean isRegistered = userService.register(user);
        if (isRegistered) {
            System.out.println("✅ Registration successful. Please log in.");
        } else {
            System.out.println("⚠️ Registration failed. Try again.");
        }
    }

}
