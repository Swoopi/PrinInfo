package com.cs336.dao;

import java.sql.Timestamp;
import com.cs336.dao.ApplicationDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Item {
    private int itemId;
    private int sellerId;
    private String title;
    private String description;
    private double startingPrice;
    private double bidIncrement;
    private double currentBid;
    private int currentBidUserId;
    private Timestamp startingTime;
    private Timestamp closingTime;
    private String status;
    private String itemType;  // Field to store the type of item.

    // Constructor for basic item creation
    public Item(String title, double startingPrice, Timestamp closingTime) {
        this.title = title;
        this.startingPrice = startingPrice;
        this.closingTime = closingTime;
    }

    // Full constructor including all fields
    public Item(int itemId, int sellerId, String title, String description, double startingPrice, double currentBid,
            int currentBidUserId, Timestamp startingTime, Timestamp closingTime, String status, String itemType) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentBid = currentBid;
        this.currentBidUserId = currentBidUserId;
        this.startingTime = startingTime;
        this.closingTime = closingTime;
        this.status = status;
        this.itemType = itemType;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public static void removeAuction(int itemId) throws SQLException {
        String sql = "UPDATE items SET status = 'removed' WHERE item_id = ?";
        try (Connection con = new ApplicationDB().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }
}
