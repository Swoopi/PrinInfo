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
        String query = "SELECT * FROM Items WHERE closing_time > NOW()";
        try (Connection con = db.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Item item = new Item(
                    rs.getString("title"),
                    rs.getDouble("starting_price"),
                    rs.getTimestamp("closing_time")
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
}
