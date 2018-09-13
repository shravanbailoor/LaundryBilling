package com.example.laundrybilling;

import java.io.Serializable;

public class Items implements Serializable {

    String typeWork;
    String itemType;
    String itemSubType;
    double price;
    int quantity;
    double itemTotalPrice;

    public Items(String typeWork, String itemType, String itemSubType, double price, int quantity, double itemTotalPrice) {
        this.typeWork = typeWork;
        this.itemType = itemType;
        this.itemSubType = itemSubType;
        this.price = price;
        this.quantity = quantity;
        this.itemTotalPrice = itemTotalPrice;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(String typeWork) {
        this.typeWork = typeWork;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemSubType() {
        return itemSubType;
    }

    public void setItemSubType(String itemSubType) {
        this.itemSubType = itemSubType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
