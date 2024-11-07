package com.onlinebookstore;

import com.onlinebookstore.controllers.AdminController;
import com.onlinebookstore.controllers.CustomerController;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("Main Menu:");
            System.out.println("1. Login as Admin");
            System.out.println("2. Continue as Customer");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1 -> loginAsAdmin();
                case 2 -> CustomerController.showCustomerMenu();
                case 3 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void loginAsAdmin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (AdminController.login(username, password)) {
            AdminController.showAdminMenu();
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }
}