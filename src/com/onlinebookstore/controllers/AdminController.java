package com.onlinebookstore.controllers;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.models.Order;
import com.onlinebookstore.services.BookService;
import com.onlinebookstore.services.OrderService;
import com.onlinebookstore.structures.CustomArrayList;

import java.util.Scanner;

public class AdminController {
    private static final BookService bookService = BookService.getInstance();
    private static final OrderService orderService = OrderService.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static boolean login(String username, String password) {
        // Replace with actual admin credentials check
        return "admin".equals(username) && "password".equals(password);
    }

    public static void showAdminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Admin Menu:");
            System.out.println("1. View all orders");
            System.out.println("2. Add book");
            System.out.println("3. View all books");
            System.out.println("4. Approve orders");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); 
            switch (option) {
                case 1 -> viewAllOrders();
                case 2 -> addBook();
                case 3 -> viewAllBooks();
                case 4 -> approveOrders();
                case 5 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void viewAllOrders() {
        System.out.println("All Orders:");
        CustomArrayList<Order> orders = orderService.getAllOrders();
        if (orders.size() == 0) {
            System.out.println("No orders found.");
            return;
        }
        orders.sort(new CustomArrayList.CustomComparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getCustomerName().compareToIgnoreCase(o2.getCustomerName());
            }
        });
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println("Order ID: " + order.getId());
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Shipping Address: " + order.getShippingAddress());
            CustomArrayList<Integer> bookIds = order.getBookIds();
            CustomArrayList<Integer> quantities = order.getQuantities();
            double totalPrice = 0;
            for (int j = 0; j < bookIds.size(); j++) {
                if (quantities.get(j) > 0) {
                    Book book = bookService.getBookById(bookIds.get(j));
                    if (book != null) {
                        double finalPrice = quantities.get(j) * book.getPrice();
                        totalPrice += finalPrice;
                        System.out.println("Book Name: " + book.getTitle() + ", Quantity: " + quantities.get(j) + ", Price: " + book.getPrice() + ", Final Price: " + finalPrice);
                    }
                }
            }
            System.out.println("Total Price: " + totalPrice);
            System.out.println("Approved: " + (order.isApproved() ? "Yes" : "No"));
            System.out.println();
        }
    }

    private static void approveOrders() {
        System.out.println("Approve Orders:");
        CustomArrayList<Order> orders = orderService.getAllOrders();
        if (orders.size() == 0) {
            System.out.println("No orders found.");
            return;
        }
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println("Order ID: " + order.getId() + ", Customer Name: " + order.getCustomerName() + ", Approved: " + (order.isApproved() ? "Yes" : "No"));
        }
        int orderId = getIntInput("Enter order ID to approve (or 0 to return): ");
        if (orderId == 0) {
            return;
        }
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.println("Order Details:");
            System.out.println("Order ID: " + order.getId());
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Shipping Address: " + order.getShippingAddress());
            CustomArrayList<Integer> bookIds = order.getBookIds();
            CustomArrayList<Integer> quantities = order.getQuantities();
            double totalPrice = 0;
            for (int j = 0; j < bookIds.size(); j++) {
                if (quantities.get(j) > 0) {
                    Book book = bookService.getBookById(bookIds.get(j));
                    if (book != null) {
                        double finalPrice = quantities.get(j) * book.getPrice();
                        totalPrice += finalPrice;
                        System.out.println("Book Name: " + book.getTitle() + ", Quantity: " + quantities.get(j) + ", Price: " + book.getPrice() + ", Final Price: " + finalPrice);
                    }
                }
            }
            System.out.println("Total Price: " + totalPrice);
            System.out.println("Approved: " + (order.isApproved() ? "Yes" : "No"));
            System.out.print("Do you want to approve this order? (yes/no): ");
            String approve = scanner.nextLine().trim().toLowerCase();
            if (approve.equals("yes") || approve.equals("y")) {
                order.setApproved(true);
                System.out.println("Order ID " + orderId + " has been approved.");
            } else {
                System.out.println("Order approval cancelled.");
            }
        } else {
            System.out.println("Order not found.");
        }
    }

    private static void addBook() {
        String title = getInput("Enter book title: ");
        String author = getInput("Enter author: ");
        int quantity = getIntInput("Enter quantity: ");
        double price = getDoubleInput("Enter price: ");
        bookService.addBook(new Book(title, author, quantity, price));
    }

    private static void viewAllBooks() {
        System.out.println("All Books:");
        CustomArrayList<Book> books = bookService.getAllBooks();
        books.sort(new CustomArrayList.CustomComparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return b1.getTitle().compareToIgnoreCase(b2.getTitle());
            }
        });
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + book.getQuantity() + ", Price: " + book.getPrice());
        }

        while (true) {
            System.out.println("Search Options:");
            System.out.println("1. Search by Title");
            System.out.println("2. Search by Author");
            System.out.println("3. Return to Menu");
            System.out.print("Choose an option: ");
            int searchOption = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (searchOption == 3) {
                return;
            }

            String searchQuery = getInput("Enter search query: ");
            CustomArrayList<Book> searchResults = new CustomArrayList<>();

            switch (searchOption) {
                case 1 -> {
                    for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                            searchResults.add(books.get(i));
                        }
                    }
                }
                case 2 -> {
                    for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).getAuthor().toLowerCase().contains(searchQuery.toLowerCase())) {
                            searchResults.add(books.get(i));
                        }
                    }
                }
                default -> {
                    System.out.println("Invalid option. Please try again.");
                    continue;
                }
            }

            if (!searchResults.isEmpty()) {
                System.out.println("Search Results:");
                for (int i = 0; i < searchResults.size(); i++) {
                    Book book = searchResults.get(i);
                    System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + book.getQuantity() + ", Price: " + book.getPrice());
                }
            } else {
                System.out.println("No books found matching the search criteria.");
            }
        }
    }

    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                break;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        return input;
    }

    private static double getDoubleInput(String prompt) {
        double input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                input = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                break;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        return input;
    }
}