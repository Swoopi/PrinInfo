package com.cs336.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private ApplicationDB db;

    public ItemDAO() {
        this.db = new ApplicationDB();
    }

    public List<Item> getActiveItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT item_id, seller_id, title, description, starting_price, current_bid, current_bid_user_id, starting_time, closing_time, status FROM Items WHERE closing_time > NOW()";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item item = new Item(
                    rs.getInt("item_id"),
                    rs.getInt("seller_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDouble("starting_price"),
                    rs.getDouble("current_bid"),
                    rs.getInt("current_bid_user_id"),
                    rs.getTimestamp("starting_time"),
                    rs.getTimestamp("closing_time"),
                    rs.getString("status")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Consider logging this error or throwing a custom exception
        }
        return items;
    }


    

    public List<Item> getItemsByUserId(int userId) {
    	System.out.println("Querying items for user ID: " + userId);
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE seller_id = ? AND closing_time > NOW()";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    double startingPrice = rs.getDouble("starting_price");
                    Timestamp closingTime = rs.getTimestamp("closing_time");
                    items.add(new Item(title, startingPrice, closingTime));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Consider logging this error or throwing a custom exception
        }
        return items;
    }
    public List<Item> searchItems(String query) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE title LIKE ? AND closing_time > NOW()";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int sellerId = rs.getInt("seller_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    double startingPrice = rs.getDouble("starting_price");
                    double currentBid = rs.getDouble("current_bid");
                    int currentBidUserId = rs.getInt("current_bid_user_id");
                    Timestamp startingTime = rs.getTimestamp("starting_time");
                    Timestamp closingTime = rs.getTimestamp("closing_time");
                    String status = rs.getString("status");
                    items.add(new Item(itemId, sellerId, title, description, startingPrice, currentBid, currentBidUserId, startingTime, closingTime, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Consider logging this error or throwing a custom exception
        }
        return items;
    }
    

}
