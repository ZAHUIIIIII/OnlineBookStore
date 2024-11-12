package com.onlinebookstore.controllers;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.models.Order;
import com.onlinebookstore.services.BookService;
import com.onlinebookstore.services.OrderService;
import com.onlinebookstore.structures.CustomArrayList;
import com.onlinebookstore.structures.CustomStack;
import com.onlinebookstore.utils.Print;
import java.util.Scanner;

public class AdminController {
  private static final BookService bookService = BookService.getInstance();
  private static final OrderService orderService = OrderService.getInstance();
  private static final Scanner scanner = new Scanner(System.in);
  private static final CustomStack<String> actionHistory = new CustomStack<>();

  public static boolean login(String username, String password) {
    // Replace with actual admin credentials check
    return "admin".equals(username) && "password".equals(password);
  }

  public static void showAdminMenu() {
    boolean running = true;
    while (running) {
      System.out.println("Admin Menu:");
      System.out.println("1. View all orders");
      System.out.println("2. Edit Book");
      System.out.println("3. View all books");
      System.out.println("4. Process Orders");
      System.out.println("5. Undo last action");
      System.out.println("6. Exit");
      System.out.print("Choose an option: ");
      int option = scanner.nextInt();
      scanner.nextLine();
      switch (option) {
        case 1 -> viewAllOrders();
        case 2 -> editBook();
        case 3 -> viewAllBooks();
        case 4 -> processOrders();
        case 5 -> undoLastAction();
        case 6 -> running = false;
        default -> System.out.println("Invalid option");
      }
    }
  }

  private static void viewAllOrders() {
    System.out.println("All Orders:");
    CustomArrayList<Order> orders = orderService.getAllOrders();
    if (orders.isEmpty()) {
      System.out.println("No orders found.");
      return;
    }
    orders.mergeSort((Order o1, Order o2) -> o1.getCustomerName().compareToIgnoreCase(o2.getCustomerName()));
    for (Order order : orders) {
      displayOrderDetails(order);
    }
  }

  private static void processOrders() {
    System.out.println("Process Orders:");
    CustomArrayList<Order> orders = orderService.getAllOrders();
    if (orders.isEmpty()) {
      System.out.println("No orders found.");
      return;
    }
    for (Order order : orders) {
      // System.out.println("Order ID: " + order.getId() + ", Customer Name: " + order.getCustomerName() + ", Approved: " + (order.isApproved() ? "Yes" : "No"));
        Print.printOrder(order);
    }
    int orderId = getIntInput("Enter order ID to approve and process (or 0 to return): ");
    if (orderId == 0) {
      return;
    }
    Order order = orderService.getOrderById(orderId);
    if (order != null) {
      displayOrderDetails(order);
      System.out.print("Do you want to approve and process this order? (yes/no): ");
      String approve = scanner.nextLine().trim().toLowerCase();
      if (approve.equals("yes") || approve.equals("y")) {
        order.setApproved(true);
        actionHistory.push("Approved and processed order ID: " + orderId);
        System.out.println("Order ID " + orderId + " has been approved and processed.");
        orderService.processNextOrder();
      } else {
        System.out.println("Order approval and processing cancelled.");
      }
    } else {
      System.out.println("Order not found.");
    }
  }


  private static void addBook() {
    String title;
    while (true) {
        title = getInput("Enter book title: ");
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Book title cannot be empty. Please try again.");
        } else if (bookService.getBookByTitle(title) != null) {
            System.out.println("A book with this title already exists. Please enter a different title.");
        } else {
            break;
        }
    }

    String author;
    while (true) {
      author = getInput("Enter author: ");
      if (author == null || author.trim().isEmpty()) {
        System.out.println("Author cannot be empty. Please try again.");
      } else {
        break;
      }
    }

    int quantity;
    while (true) {
      quantity = getIntInput("Enter quantity: ");
      if (quantity <= 0) {
        System.out.println("Quantity cannot be less than or equal to 0. Please enter a valid quantity.");
      } else {
        break;
      }
    }

    double price;
    while (true) {
      price = getDoubleInput("Enter price: ");
      if (price < 0) {
        System.out.println("Price cannot be negative. Please enter a valid price.");
      } else {
        break;
      }
    }

    Book newBook = new Book(title, author, quantity, price);
    bookService.addBook(newBook);
    System.out.println("Book added successfully: " + newBook);
  }


  private static void editBook() {
    while (true) {
      System.out.println("Edit Book Options:");
      System.out.println("1. Add Book");
      System.out.println("2. Update Book");
      System.out.println("3. Delete Book");
      System.out.println("4. Return to Menu");
      System.out.print("Choose an option: ");
      int option = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (option) {
        case 1 -> addBook();
        case 2 -> {
          showAllBooks();
          updateBook();
        }
        case 3 -> {
          showAllBooks();
          deleteBook();
        }
        case 4 -> {
          return;
        } // Return to the main menu
        default -> System.out.println("Invalid option. Please try again.");
      }
    }
  }

  private static void updateBook() {
    int bookId = getIntInput("Enter book ID to update (or 0 to cancel): ");
    if (bookId == 0) {
      System.out.println("Update action canceled.");
      return;
    }
    Book book = bookService.getBookById(bookId);
    if (book == null) {
      System.out.println("Book not found.");
      return;
    }


    // Display current book information
    System.out.println("Current Book Information:");
    System.out.println("Title: " + book.getTitle());
    System.out.println("Author: " + book.getAuthor());
    System.out.println("Quantity: " + book.getQuantity());
    System.out.println("Price: " + book.getPrice());

    String title = getInput("Enter new book title (or 0 to keep current): ");
    if (!title.equals("0")) {
      book.setTitle(title);
    }

    String author = getInput("Enter new author (or 0 to keep current): ");
    if (!author.equals("0")) {
      book.setAuthor(author);
    }

    String quantityInput = getInput("Enter new quantity (or 0 to keep current): ");
    if (!quantityInput.equals("0")) {
      try {
        int quantity = Integer.parseInt(quantityInput);
        if (quantity >= 0) {
          book.setQuantity(quantity);
        } else {
          System.out.println("Quantity cannot be negative. Keeping current quantity.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid quantity input. Keeping current quantity.");
      }
    }

    String priceInput = getInput("Enter new price (or 0 to keep current): ");
    if (!priceInput.equals("0")) {
      try {
        double price = Double.parseDouble(priceInput);
        if (price >= 0) {
          book.setPrice(price);
        } else {
          System.out.println("Price cannot be negative. Keeping current price.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid price input. Keeping current price.");
      }
    }

    bookService.updateBook(book);
    System.out.println("Book updated successfully: " + book);
  }


  private static void deleteBook() {
    int bookId = getIntInput("Enter book ID to delete (or 0 to cancel): ");
    if (bookId == 0) {
      System.out.println("Delete action canceled.");
      return;
    }
    Book book = bookService.getBookById(bookId);
    if (book == null) {
      System.out.println("Book not found.");
      return;
    }
    bookService.deleteBook(bookId);
    System.out.println("Book deleted successfully: " + book.getTitle());
  }


  private static void viewAllBooks() {
    CustomArrayList<Book> books = bookService.getAllBooks();
    showAllBooks();
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
      switch (searchOption) {
        case 1, 2 -> searchBooks(books, searchOption);
        default -> System.out.println("Invalid option. Please try again.");
      }
    }
  }


  private static void searchBooks(CustomArrayList<Book> books, int searchOption) {
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
        return;
      }
    }
    if (!searchResults.isEmpty()) {
      System.out.println("Search Results:");
      for (int i = 0; i < searchResults.size(); i++) {
        Book book = searchResults.get(i);

        //
        Print.printBook(book, i);
      }
    } else {
      System.out.println("No books found matching the search criteria.");
    }
  }


  private static void displayOrderDetails(Order order) {
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
          //
          System.out.println("Book Name: " + book.getTitle() + ", Quantity: " + quantities.get(j) + ", Price: " + book.getPrice() + ", Final Price: " + finalPrice);
        }
      }
    }

    System.out.println("Total Price: " + totalPrice);
    System.out.println("Approved: " + (order.isApproved() ? "Yes" : "No"));
  }


  private static void undoLastAction() {
    if (actionHistory.isEmpty()) {
      System.out.println("No actions to undo.");
      return;
    }
    String lastAction = actionHistory.pop();
    System.out.println("Undoing last action: " + lastAction);

    if (lastAction.startsWith("Approved and processed order ID: ")) {
      int orderId = Integer.parseInt(lastAction.substring("Approved and processed order ID: ".length()));
      Order order = orderService.getOrderById(orderId);
      if (order != null) {
        order.setApproved(false);
        System.out.println("Order ID " + orderId + " has been unapproved.");
      }
    } else if (lastAction.startsWith("Processed order ID: ")) {
      int orderId = Integer.parseInt(lastAction.substring("Processed order ID: ".length()));
      Order order = orderService.getOrderById(orderId);
      if (order != null) {
        orderService.addOrderToQueue(order);
        System.out.println("Order ID " + orderId + " has been added back to the processing queue.");
      }
    } else {
      System.out.println("No undo action available for: " + lastAction);
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


  private static void showAllBooks() {
    System.out.println("All Books:");
    CustomArrayList<Book> books = bookService.getAllBooks();
    books.mergeSort((Book b1, Book b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
    for (int i = 0; i < books.size(); i++) {
      Book book = books.get(i);
      //
      Print.printBook(book, i);
    }
  }
}