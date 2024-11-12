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

    private OrderService() {
    }

    public static OrderService getInstance() {
        return instance;
    }

    public void placeOrder(Order order) {
        order.setId(nextId++);
        orders.add(order);
        orderQueue.enqueue(order);
        // System.out.println("Order placed for " + order.getCustomerName());
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

    public void cancelOrder(int orderId) {
        Order order = getOrderById(orderId);
        if (order != null && !order.isApproved()) {
            CustomArrayList<Integer> bookIds = order.getBookIds();
            CustomArrayList<Integer> quantities = order.getQuantities();
            for (int i = 0; i < bookIds.size(); i++) {
                Book book = BookService.getInstance().getBookById(bookIds.get(i));
                if (book != null) {
                    book.setQuantity(book.getQuantity() + quantities.get(i));
                }
            }
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId() == orderId) {
                    orders.remove(i);
                    break;
                }
            }
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

    public Order processNextOrder() {
        if (!orderQueue.isEmpty()) {
            return orderQueue.dequeue();
        } else {
            System.out.println("No orders to process.");
            return null;
        }
    }
    public void addOrderToQueue(Order order) {
        orderQueue.enqueue(order);
    }
}