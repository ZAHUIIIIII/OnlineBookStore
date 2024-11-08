package com.onlinebookstore;

import com.onlinebookstore.controllers.AdminController;
import com.onlinebookstore.controllers.CustomerController;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> scanner.close()));
        boolean running = true;
        while (running) {
            System.out.println("Main Menu:");
            System.out.println("1. Login as Admin");
            System.out.println("2. Continue as Customer");
            System.out.println("3. Exit");
            int option = getValidatedOption();

            switch (option) {
                case 1 -> loginAsAdmin();
                case 2 -> CustomerController.showCustomerMenu();
                case 3 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void loginAsAdmin() {
        String[] credentials = getCredentials();
        if (AdminController.login(credentials[0], credentials[1])) {
            AdminController.showAdminMenu();
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static String[] getCredentials() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        return new String[]{username, password};
    }

    private static int getValidatedOption() {
        int option = -1;
        while (option < 1 || option > 3) {
            try {
                System.out.print("Choose an option (1-3): ");
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            }
        }
        return option;
    }
}
