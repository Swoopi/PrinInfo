package com.cs336.dao;

import java.sql.Timestamp;

public class Item {
    private int itemId;
    private int sellerId;  // Seller ID field
    private String title;
    private String description; // To store a detailed description of the item.
    private double startingPrice;
    private double bidIncrement;  
    private double currentBid;
    private int currentBidUserId;
    private Timestamp startingTime; // To track when the auction starts.
    private Timestamp closingTime;
    private String status; // To track the status of the item, such as active, sold, etc.
    private String[] features;
    private String[] featureVaklues;

    // Constructor for basic item creation
    public Item(String title, double startingPrice, Timestamp closingTime) {
        this.title = title;
        this.startingPrice = startingPrice;
        this.closingTime = closingTime;
    }

    // Full constructor to initialize all fields including seller ID
    public Item(int itemId, int sellerId, String title, String description, double startingPrice, double currentBid, int currentBidUserId, Timestamp startingTime, Timestamp closingTime, String status) {
        this.itemId = itemId;
        this.sellerId = sellerId;  // Initialize seller ID
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentBid = currentBid;
        this.currentBidUserId = currentBidUserId;
        this.startingTime = startingTime;
        this.closingTime = closingTime;
        this.status = status;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSellerId() {  // Getter for seller ID
        return sellerId;
    }

    public void setSellerId(int sellerId) {  // Setter for seller ID
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public int getCurrentBidUserId() {
        return currentBidUserId;
    }

    public void setCurrentBidUserId(int currentBidUserId) {
        this.currentBidUserId = currentBidUserId;
    }

    public Timestamp getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Timestamp startingTime) {
        this.startingTime = startingTime;
    }

    public Timestamp getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Timestamp closingTime) {
        this.closingTime = closingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public double getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(double bidIncrement) {
        this.bidIncrement = bidIncrement;
    }
}
