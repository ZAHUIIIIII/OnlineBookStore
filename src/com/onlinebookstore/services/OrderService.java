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
        for (int id : bookIds) {
            for (int i = 0; i < allBooks.size(); i++) {
                Book book = allBooks.get(i);
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

    public void cancelOrder(int orderId) {
        Order order = getOrderById(orderId);
        if (order != null && !order.isApproved()) {
            // Remove from orders list
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId() == orderId) {
                    orders.remove(i);
                    break;
                }
            }
            // Remove from queue if it exists
            CustomQueue<Order> tempQueue = new CustomQueue<>();
            while (!orderQueue.isEmpty()) {
                Order queuedOrder = orderQueue.dequeue();
                if (queuedOrder.getId() != orderId) {
                    tempQueue.enqueue(queuedOrder);
                }
            }
            while (!tempQueue.isEmpty()) {
                orderQueue.enqueue(tempQueue.dequeue());
            }
            System.out.println("Order ID " + orderId + " has been canceled.");
        } else {
            System.out.println("Order not found or already approved.");
        }
    }
}