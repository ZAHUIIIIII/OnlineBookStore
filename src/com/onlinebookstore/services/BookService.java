package com.onlinebookstore.services;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.structures.CustomArrayList;

public class BookService {
    private static final BookService instance = new BookService();
    private final CustomArrayList<Book> books = new CustomArrayList<>();
    private int nextId = 1;

    private BookService() {
        initializeBooks();
    }

    public static BookService getInstance() {
        return instance;
    }

    private void initializeBooks() {
        String[] titles = {
                "Java Programming", "Effective Java", "Clean Code", "Head First Java", "Java Concurrency in Practice",
                "Java: The Complete Reference", "Spring in Action", "Hibernate in Action", "Java Performance", "Java Puzzlers",
                "Java 8 in Action", "Java SE 8 for the Really Impatient", "Java: A Beginner's Guide", "Java Network Programming", "Java Cookbook",
                "Java Design Patterns", "Java EE 7 Essentials", "JavaFX 8: Introduction by Example", "Java Web Services: Up and Running", "Java XML and JSON"
        };
        String[] authors = {
                "Joshua Bloch", "Joshua Bloch", "Robert C. Martin", "Kathy Sierra", "Brian Goetz",
                "Herbert Schildt", "Craig Walls", "Christian Bauer", "Scott Oaks", "Joshua Bloch",
                "Raoul-Gabriel Urma", "Cay S. Horstmann", "Herbert Schildt", "Elliotte Rusty Harold", "Ian F. Darwin",
                "Vaskaran Sarcar", "Arun Gupta", "Carl Dea", "Martin Kalin", "Jeff Friesen"
        };
        int[] quantities = {10, 15, 20, 5, 8, 12, 7, 9, 14, 6, 11, 13, 16, 4, 18, 3, 17, 19, 2, 1};
        double[] prices = {45.99, 55.99, 39.99, 49.99, 59.99, 35.99, 29.99, 65.99, 75.99, 85.99, 25.99, 45.99, 55.99, 65.99, 75.99, 85.99, 95.99, 105.99, 115.99, 125.99};
        
        for (int i = 0; i < titles.length; i++) {
            books.add(new Book(titles[i], authors[i], quantities[i], prices[i]));
        }
    }

    public CustomArrayList<Book> getAllBooks() {
        // O(1) time complexity for returning the list reference
        return books;
    }

    public Book getBookById(int id) {
        // O(n) time complexity for linear search
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                return books.get(i);
            }
        }
        return null;
    }

    public void addBook(Book book) {
        // O(1) average time complexity for adding a book
        book.setId(nextId++);
        books.add(book);
    }

    public void updateBook(Book book) {
        // O(n) time complexity for updating a book
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                return;
            }
        }
    }

    public void deleteBook(int id) {
        // O(n) time complexity for removing a book
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
                return;
            }
        }
    }

    // New method: Search books by title
    public CustomArrayList<Book> searchBooksByTitle(String title) {
        // O(n) time complexity for linear search
        CustomArrayList<Book> matchingBooks = new CustomArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equalsIgnoreCase(title)) {
                matchingBooks.add(books.get(i));
            }
        }
        return matchingBooks;
    }

    // New method: Sort books by price
    public void sortBooksByPrice() {
        // O(n log n) time complexity for merge sort
        CustomArrayList.CustomComparator<Book> comparator = (b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice());
        books.mergeSort(comparator);
    }
}
