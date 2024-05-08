package com.cs336.dao;
import java.sql.Timestamp;
public class Bid {
    private int bidId;
    private int itemId;
    private int userId;
    private String bidType;  // 'manual' or 'automatic'
    private double bidAmount;
    private Double autoIncrement;  // nullable, used only for automatic bids
    private Double autoLimit;      // nullable, maximum bid amount for automatic bids
    private Timestamp bidTime;

    // Constructor
    public Bid(int bidId, int itemId, int userId, String bidType, double bidAmount, Double autoIncrement, Double autoLimit, Timestamp bidTime ) {
        this.bidId = bidId;
        this.itemId = itemId;
        this.userId = userId;
        this.bidType = bidType;
        this.bidAmount = bidAmount;
        this.autoIncrement = autoIncrement;
        this.autoLimit = autoLimit;
        this.bidTime = bidTime;
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

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Double getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Double autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Double getAutoLimit() {
        return autoLimit;
    }

    public void setAutoLimit(Double autoLimit) {
        this.autoLimit = autoLimit;
    }
    public Timestamp getBidTime() {
        return bidTime;
    }

    public void setBidTime(Timestamp bidTime) {
        this.bidTime = bidTime;
    }
}
