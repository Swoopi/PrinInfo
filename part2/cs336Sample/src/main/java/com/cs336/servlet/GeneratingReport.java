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
        String query = "SELECT SUM(sale_price) AS total_earnings FROM sales";
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
        String query = "SELECT items.title, SUM(sales.sale_price) AS earnings FROM sales JOIN items ON sales.item_id = items.item_id GROUP BY items.item_id";
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
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT username, SUM(sale_price) AS total_spent FROM sales JOIN users ON sales.buyer_id = users.userid GROUP BY buyer_id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("username") + " - Total Spent: $" + rs.getDouble("total_spent"));
            }
        }
        return users;
    }


    public List<String> getBestBuyers() throws SQLException {
        List<String> buyers = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT username, SUM(sale_price) AS total_spent FROM sales JOIN users ON sales.buyer_id = users.userid GROUP BY buyer_id ORDER BY total_spent DESC LIMIT 10")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                buyers.add(rs.getString("username") + " - Total Spent: $" + rs.getDouble("total_spent"));
            }
        }
        return buyers;
    }

    public List<String> getBestSellingItems() throws SQLException {
        List<String> results = new ArrayList<>();
        String query = "SELECT items.title, COUNT(*) AS number_sold, SUM(sales.sale_price) AS total_earnings FROM sales JOIN items ON sales.item_id = items.item_id GROUP BY items.item_id ORDER BY number_sold DESC, total_earnings DESC LIMIT 10";
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
