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
            e.printStackTrace(); // Better handling/logging recommended
        }
        return items;
    }
}
