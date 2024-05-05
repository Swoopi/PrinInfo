package com.cs336.dao;

public class Bid {
    private int bidId;
    private int itemId;
    private int userId;
    private double bidAmount;

    // Constructor
    public Bid(int bidId, int itemId, int userId, double bidAmount) {
        this.bidId = bidId;
        this.itemId = itemId;
        this.userId = userId;
        this.bidAmount = bidAmount;
    }

    // Getters and Setters
    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
}
