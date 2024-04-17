package com.cs336.dao;

public class Item {
    private String title;
    private double startingPrice;
    private java.sql.Timestamp closingTime;

    // Constructor
    public Item(String title, double startingPrice, java.sql.Timestamp closingTime) {
        this.title = title;
        this.startingPrice = startingPrice;
        this.closingTime = closingTime;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public java.sql.Timestamp getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(java.sql.Timestamp closingTime) {
        this.closingTime = closingTime;
    }
}
