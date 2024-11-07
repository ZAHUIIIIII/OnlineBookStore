package com.onlinebookstore.services;

import com.onlinebookstore.models.Book;
import com.onlinebookstore.models.Order;
import com.onlinebookstore.structures.CustomArrayList;
import com.onlinebookstore.structures.CustomQueue;

public class OrderService {
    private static final OrderService instance = new OrderService();
    private final CustomArrayList<Order> orders = new CustomArrayList<>();
    private int nextId = 1;
    private final CustomQueue<Order> orderQueue = new CustomQueue<>();

    private OrderService() {}

    public static OrderService getInstance() {
        return instance;
    }

    public void placeOrder(Order order) {
        // O(1) time complexity for adding an order (average case)
        order.setId(nextId++);
        orders.add(order);
        orderQueue.enqueue(order);
        System.out.println("Order placed for " + order.getCustomerName());
    }

    public CustomArrayList<Order> getAllOrders() {
        // O(1) time complexity for returning the list reference
        return orders;
    }

    public Order getOrderById(int id) {
        // O(n) time complexity for linear search
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getId() == id) {
                return order;
            }
        }
        return null; // Not found
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

    public void sortOrdersById() {
        orders.mergeSort(new CustomArrayList.CustomComparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
    }

    public void addOrderToQueue(Order order) {
        orderQueue.enqueue(order);
    }

    public Order processNextOrder() {
        if (!orderQueue.isEmpty()) {
            return orderQueue.dequeue();
        } else {
            System.out.println("No orders to process.");
            return null;
        }
    }
}