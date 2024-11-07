package com.onlinebookstore.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private int quantity;
    private double price;

    public Book(String title, String author, int quantity, double price) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters with O(1) time complexity
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    // Utility method to check if the book's data is valid
    public boolean isValid() {
        // O(1) time complexity for simple validation checks
        return quantity >= 0 && price >= 0;
    }

    @Override
    public String toString() {
        // O(1) time complexity for creating a string representation
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
