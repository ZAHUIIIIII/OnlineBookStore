package com.onlinebookstore.utils;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.models.Order;

public class Print {
    public static void printOrder(Order order) {
        System.out.println("Order ID: " + order.getId() + ", Customer Name: " + order.getCustomerName() + ", Approved: " + (order.isApproved() ? "Yes" : "No"));
    }

    public static void printBook(Book book, int index) {
        System.out.println((index + 1) + ". Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Quantity: " + book.getQuantity() + ", Price: " + book.getPrice());
    }
}