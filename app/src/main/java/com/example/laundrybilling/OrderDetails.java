package com.example.laundrybilling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderDetails implements Serializable {
    String orderNumber;
    String customerName;
    long phoneNumber;
    List<Items> items;
    double totalCost;
    String deliveryDate;
    String orderComments;

    public OrderDetails(String orderNumber, String customerName, long phoneNumber, List<Items> items, double totalCost, String deliveryDate, String orderComments) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.totalCost = totalCost;
        this.deliveryDate = deliveryDate;
        this.orderComments = orderComments;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments;
    }
}
