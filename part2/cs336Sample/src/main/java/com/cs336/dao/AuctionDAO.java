package com.cs336.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionDAO {
    private ApplicationDB db;

    public AuctionDAO() {
        this.db = new ApplicationDB();
    }

    public List<Auction> getActiveAuctions() {
        List<Auction> auctions = new ArrayList<>();
        String query = "SELECT auctionID, itemID, sellerID, title, description, starting_price, current_bid, current_bid_userID, starting_time, closing_time, auction_status FROM Auctions WHERE closing_time > NOW()";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Auction auction= new Auction(
                	rs.getInt("auctionID"),
                	rs.getInt("itemID"),
                    rs.getInt("sellerID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDouble("starting_price"),
                    rs.getDouble("current_bid"),
                    rs.getInt("current_bid_userID"),
                    rs.getTimestamp("starting_time"),
                    rs.getTimestamp("closing_time"),
                    rs.getString("auction_status")
                );
                auctions.add(auction);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Consider logging this error or throwing a custom exception
        }
        return auctions;
    }


    
/*
    public List<Item> getItemsByUserId(int userId) {
    	System.out.println("Querying items for user ID: " + userId);
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM Auctions WHERE sellerID = ? AND closing_time > NOW()";
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
    */
}
