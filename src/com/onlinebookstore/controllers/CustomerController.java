package com.onlinebookstore.controllers;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.models.Order;
import com.onlinebookstore.services.BookService;
import com.onlinebookstore.services.OrderService;
import com.onlinebookstore.structures.CustomArrayList;
import java.util.Scanner;

public class CustomerController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = BookService.getInstance();
    private static final OrderService orderService = OrderService.getInstance();

    public static void showCustomerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Customer Menu:");
            System.out.println("1. View All Books");
            System.out.println("2. Order Books");
            System.out.println("3. View My Orders");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1 -> viewAllBooks(); // O(n log n) for sorting and O(n) for display
                case 2 -> viewAllBooksAndAddToCart();
                case 3 -> viewMyOrders();
                case 4 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewAllBooks() {
        CustomArrayList<Book> allBooks = bookService.getAllBooks();
        allBooks.mergeSort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle())); // O(n log n) for sorting

        System.out.println("All Books:");
        for (int i = 0; i < allBooks.size(); i++) { // O(n) for display
            Book book = allBooks.get(i);
            System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + book.getQuantity() + ", Price: " + book.getPrice());
        }
    }

    private static void viewAllBooksAndAddToCart() {
        CustomArrayList<Book> shoppingCart = new CustomArrayList<>();
        CustomArrayList<Integer> quantities = new CustomArrayList<>();
        CustomArrayList<Book> allBooks = bookService.getAllBooks();

        while (true) {

            System.out.println("Options:");
            System.out.println("1. Search by Title");
            System.out.println("2. Search by Author");
            System.out.println("3. Add Multiple Books to Cart");
            System.out.println("4. View Cart and Checkout");
            System.out.println("5. Return to Menu");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (option == 5) {
                return;
            }

            switch (option) {
                case 1, 2 -> searchAndAddBooks(allBooks, shoppingCart, quantities, option);
                case 3 -> addMultipleBooksToCart(allBooks, shoppingCart, quantities);
                case 4 -> viewCartAndCheckout(shoppingCart, quantities);
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void searchAndAddBooks(CustomArrayList<Book> allBooks, CustomArrayList<Book> shoppingCart, CustomArrayList<Integer> quantities, int searchOption) {
        String searchQuery = getInput("Enter search query: ");
        CustomArrayList<Book> searchResults = new CustomArrayList<>();

        // O(n) for linear search
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if ((searchOption == 1 && book.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) ||
                (searchOption == 2 && book.getAuthor().toLowerCase().contains(searchQuery.toLowerCase()))) {
                searchResults.add(book);
            }
        }

        if (!searchResults.isEmpty()) {
            displaySearchResults(searchResults);

            int bookNumber = getIntInput("Enter book number to add to cart (or 0 to return): ");
            if (bookNumber == 0) {
                return;
            }

            if (bookNumber < 1 || bookNumber > searchResults.size()) {
                System.out.println("Invalid book number. Please choose a valid book number.");
                return;
            }

            Book book = searchResults.get(bookNumber - 1);
            handleBookAdditionToCart(book, shoppingCart, quantities);
        } else {
            System.out.println("No books found matching the search criteria.");
        }
    }

    private static void displaySearchResults(CustomArrayList<Book> searchResults) {
        System.out.println("Search Results:");
        for (int i = 0; i < searchResults.size(); i++) { // O(n)
            Book book = searchResults.get(i);
            System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + book.getQuantity() + ", Price: " + book.getPrice());
        }
    }

    private static void handleBookAdditionToCart(Book book, CustomArrayList<Book> shoppingCart, CustomArrayList<Integer> quantities) {
        if (book.getQuantity() <= 0) {
            System.out.println("Book is out of stock. Please choose another book.");
            return;
        }

        int quantity = getIntInput("Enter quantity for book (available: " + book.getQuantity() + "): ");
        if (quantity > book.getQuantity()) {
            System.out.println("Ordered quantity exceeds available stock.");
            return;
        }

        shoppingCart.add(book);
        quantities.add(quantity);
        book.setQuantity(book.getQuantity() - quantity); // Update stock
        System.out.println("Book added to cart.");
    }

    private static void addMultipleBooksToCart(CustomArrayList<Book> allBooks, CustomArrayList<Book> shoppingCart, CustomArrayList<Integer> quantities) {
        System.out.println("All Books:");
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + book.getQuantity() + ", Price: " + book.getPrice());
        }

        while (true) {
            int bookNumber = getIntInput("Enter book number to add to cart (or 0 to finish): ");
            if (bookNumber == 0) {
                break;
            }

            if (bookNumber < 1 || bookNumber > allBooks.size()) {
                System.out.println("Invalid book number. Please choose a valid book number.");
                continue;
            }

            Book book = allBooks.get(bookNumber - 1);

            if (book.getQuantity() <= 0) {
                System.out.println("Book is out of stock. Please choose another book.");
                continue;
            }

            int quantity = getIntInput("Enter quantity for book (available: " + book.getQuantity() + "): ");
            if (quantity > book.getQuantity()) {
                System.out.println("Ordered quantity exceeds available stock.");
                continue;
            }

            shoppingCart.add(book);
            quantities.add(quantity);
            book.setQuantity(book.getQuantity() - quantity); // Update stock
            System.out.println("Book added to cart.");
        }
    }

    private static void viewCartAndCheckout(CustomArrayList<Book> shoppingCart, CustomArrayList<Integer> quantities) {
        if (shoppingCart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Your Cart:");
        double totalPrice = 0;
        for (int i = 0; i < shoppingCart.size(); i++) {
            Book book = shoppingCart.get(i);
            int quantity = quantities.get(i);
            double finalPrice = quantity * book.getPrice();
            totalPrice += finalPrice;
            System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + quantity + ", Price: " + book.getPrice() + ", Final Price: " + finalPrice);
        }
        System.out.println("Total Price: " + totalPrice);

        while (true) {
            System.out.println("Cart Options:");
            System.out.println("1. Remove Book from Cart");
            System.out.println("2. Proceed to Payment");
            System.out.println("3. Return to Order Options");
            System.out.print("Choose an option: ");
            int cartOption = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (cartOption == 3) {
                return;
            }

            switch (cartOption) {
                case 1 -> removeBookFromCart(shoppingCart, quantities);
                case 2 -> proceedToPayment(shoppingCart, quantities, totalPrice); 
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void removeBookFromCart(CustomArrayList<Book> shoppingCart, CustomArrayList<Integer> quantities) {
        int bookNumber = getIntInput("Enter book number to remove from cart: ");
        if (bookNumber < 1 || bookNumber > shoppingCart.size()) {
            System.out.println("Invalid book number. Please choose a valid book number.");
            return;
        }

        Book book = shoppingCart.get(bookNumber - 1);
        int quantity = quantities.get(bookNumber - 1);
        book.setQuantity(book.getQuantity() + quantity); // Restore the book's quantity in the inventory
        shoppingCart.remove(bookNumber - 1); // Remove the book from the cart
        quantities.remove(bookNumber - 1); // Remove the quantity from the list
        System.out.println("Book removed from cart.");
    }

    private static void proceedToPayment(CustomArrayList<Book> shoppingCart, CustomArrayList<Integer> quantities, double totalPrice) {
        System.out.println("Your Cart:");
        for (int i = 0; i < shoppingCart.size(); i++) {
            Book book = shoppingCart.get(i);
            int quantity = quantities.get(i);
            double finalPrice = quantity * book.getPrice();
            System.out.println((i + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + quantity + ", Price: " + book.getPrice() + ", Final Price: " + finalPrice);
        }
        System.out.println("Total Price: " + totalPrice);

        String shippingAddress = getInput("Enter shipping address: ");
        Order order = new Order("customer", shippingAddress, new CustomArrayList<>(), new CustomArrayList<>());
        for (int i = 0; i < shoppingCart.size(); i++) {
            order.getBookIds().add(shoppingCart.get(i).getId());
            order.getQuantities().add(quantities.get(i));
        }
        orderService.placeOrder(order);
        System.out.println("Order placed successfully. Total Price: " + totalPrice);

        shoppingCart.clear();
        quantities.clear();
        
    }

    private static void viewMyOrders() {
        System.out.println("My Orders:");
        CustomArrayList<Order> orders = orderService.getAllOrders();
        boolean hasOrders = false;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getCustomerName().equals("customer")) {
                hasOrders = true;
                System.out.println("Order ID: " + order.getId());
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

        if (!hasOrders) {
            System.out.println("You have not placed any orders.");
        }

        String searchQuery = getInput("Enter order ID to search (or press Enter to skip): ");
        if (!searchQuery.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(searchQuery);
                Order order = orderService.getOrderById(orderId);
                if (order != null && order.getCustomerName().equals("customer")) {
                    System.out.println("Order found: " + order);
                    System.out.println("Order ID: " + order.getId());
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
                } else {
                    System.out.println("Order not found.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid order ID.");
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
}