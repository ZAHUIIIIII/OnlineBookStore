package com.onlinebookstore.models;

import com.onlinebookstore.structures.CustomArrayList;

public class Order {
    private int id;
    private String customerName;
    private String shippingAddress;
    private CustomArrayList<Integer> bookIds;
    private CustomArrayList<Integer> quantities;
    private boolean approved;

    public Order(String customerName, String shippingAddress, CustomArrayList<Integer> bookIds, CustomArrayList<Integer> quantities) {
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.bookIds = bookIds;
        this.quantities = quantities;
        this.approved = false;
    }

    // Getters and setters with O(1) time complexity
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public CustomArrayList<Integer> getBookIds() {
        return bookIds;
    }

    public CustomArrayList<Integer> getQuantities() {
        return quantities;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", bookIds=" + bookIds +
                ", quantities=" + quantities +
                ", approved=" + approved +
                '}';
    }
}