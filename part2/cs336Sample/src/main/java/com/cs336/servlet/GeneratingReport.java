package com.cs336.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.cs336.dao.ApplicationDB;

public class GeneratingReport {
    private ApplicationDB db;

    public GeneratingReport() {
        db = new ApplicationDB();
    }

    public double getTotalEarnings() throws SQLException {
        String query = "SELECT SUM(current_bid) AS total_earnings FROM items WHERE status = 'sold'";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total_earnings");
            }
        }
        return 0.0;
    }



 // Method to fetch earnings per item
    public List<String> getEarningsPerItem() throws SQLException {
        List<String> results = new ArrayList<>();
        String query = "SELECT title, current_bid AS earnings FROM items WHERE status = 'sold'";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String result = "Item: " + rs.getString("title") + ", Earnings: $" + rs.getDouble("earnings");
                results.add(result);
            }
        }
        return results;
    }

    //fix this method
    public List<String> getEarningsPerItemType() throws SQLException {
        List<String> types = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT item_type, SUM(sale_price) AS total_earnings FROM sales JOIN items ON sales.item_id = items.item_id GROUP BY item_type")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                types.add(rs.getString("item_type") + " - Total Earnings: $" + rs.getDouble("total_earnings"));
            }
        }
        return types;
    }

    public List<String> getEarningsPerUser() throws SQLException {
        List<String> users = new ArrayList<>();
        String query = "SELECT u.username, SUM(i.current_bid) AS total_spent FROM items i JOIN users u ON i.current_bid_user_id = u.userid WHERE i.status = 'sold' GROUP BY i.current_bid_user_id";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(rs.getString("username") + " - Total Spent: $" + rs.getDouble("total_spent"));
            }
        }
        return users;
    }



    public List<String> getBestBuyers() throws SQLException {
        List<String> buyers = new ArrayList<>();
        String query = "SELECT u.username, SUM(i.current_bid) AS total_spent FROM items i JOIN users u ON i.current_bid_user_id = u.userid WHERE i.status = 'sold' GROUP BY i.current_bid_user_id ORDER BY total_spent DESC LIMIT 10";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                buyers.add(rs.getString("username") + " - Total Spent: $" + rs.getDouble("total_spent"));
            }
        }
        return buyers;
    }


    public List<String> getBestSellingItems() throws SQLException {
        List<String> results = new ArrayList<>();
        String query = "SELECT title, COUNT(*) AS number_sold, SUM(current_bid) AS total_earnings FROM items WHERE status = 'sold' GROUP BY item_id ORDER BY number_sold DESC, total_earnings DESC LIMIT 10";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String result = "Item: " + rs.getString("title") + ", Sold: " + rs.getInt("number_sold") + ", Total Earnings: $" + rs.getDouble("total_earnings");
                results.add(result);
            }
        }
        return results;
    }

}
