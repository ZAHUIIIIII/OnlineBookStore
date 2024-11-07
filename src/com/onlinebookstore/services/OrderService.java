package com.onlinebookstore.services;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.models.Order;
import com.onlinebookstore.structures.CustomArrayList;

public class OrderService {
    private static final OrderService instance = new OrderService();
    private final CustomArrayList<Order> orders = new CustomArrayList<>();
    private int nextId = 1;

    private OrderService() {}

    public static OrderService getInstance() {
        return instance;
    }

    public void placeOrder(Order order) {
        order.setId(nextId++);
        orders.add(order);
        System.out.println("Order placed for " + order.getCustomerName());
    }

    public CustomArrayList<Order> getAllOrders() {
        return orders;
    }

    public Order getOrderById(int id) {
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public CustomArrayList<Book> getBooksByIds(CustomArrayList<Integer> bookIds, CustomArrayList<Book> allBooks) {
        CustomArrayList<Book> books = new CustomArrayList<>();
        for (int i = 0; i < bookIds.size(); i++) {
            int id = bookIds.get(i);
            for (int j = 0; j < allBooks.size(); j++) {
                Book book = allBooks.get(j);
                if (book.getId() == id) {
                    books.add(book);
                    break;
                }
            }
        }
        return books;
    }
}