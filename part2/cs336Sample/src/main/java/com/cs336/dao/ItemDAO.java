package com.cs336.dao;

import com.cs336.dao.Item;
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
        String query = "SELECT item_id, seller_id, title, description, starting_price, current_bid, current_bid_user_id, bid_increment, starting_time, closing_time, status, item_type FROM Items WHERE closing_time > NOW() AND status = 'active'";
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
                    rs.getString("status"),
                    rs.getString("item_type")
                );
                item.setBidIncrement(rs.getDouble("bid_increment"));  // This line is crucial
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Consider logging this error or throwing a custom exception
        }
        return items;
    }


    public void checkAndCloseBids() {
        Connection con = null;
        PreparedStatement updateStmt = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false); // Start transaction for batch update

            // First, update all items that have met or exceeded the minimum price and are past the closing time
            String updateSoldQuery = "UPDATE items SET status = 'sold' WHERE closing_time <= NOW() AND status = 'active' AND current_bid >= minimum_price";
            updateStmt = con.prepareStatement(updateSoldQuery);
            int soldUpdated = updateStmt.executeUpdate();

            // Now, update all items where the current bid is below the minimum price and are past the closing time
            String updateRemovedQuery = "UPDATE items SET status = 'removed' WHERE closing_time <= NOW() AND status = 'active' AND current_bid < minimum_price";
            updateStmt = con.prepareStatement(updateRemovedQuery);
            int removedUpdated = updateStmt.executeUpdate();

            con.commit(); // Commit all changes if both operations were successful

            System.out.println("Updated " + soldUpdated + " item(s) to 'sold' status.");
            System.out.println("Updated " + removedUpdated + " item(s) to 'removed' status.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            try {
                if (con != null) {
                    con.rollback(); // Rollback transaction if any error occurs
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception on rollback: " + ex.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                if (updateStmt != null) updateStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception during resource cleanup: " + e.getMessage());
            }
        }
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
    public boolean updateBid(int itemId, int userId, double bidAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement updatePs = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false); // Start transaction

            // Fetch the current bid and bid increment
            String query = "SELECT current_bid, bid_increment FROM items WHERE item_id = ? AND status = 'active'";
            ps = con.prepareStatement(query);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();

            if (rs.next()) {
                double currentBid = rs.getDouble("current_bid");
                double bidIncrement = rs.getDouble("bid_increment");

                System.out.println("Current Bid: " + currentBid + ", Bid Increment: " + bidIncrement + ", New Bid: " + bidAmount);
                
                // Check if the new bid is at least current bid plus bid increment
                if (bidAmount >= (currentBid + bidIncrement)) {
                	String updateQuery = "UPDATE items SET current_bid = ?, current_bid_user_id = ? WHERE item_id = ?";
                    updatePs = con.prepareStatement(updateQuery);
                    updatePs.setDouble(1, bidAmount);
                    updatePs.setInt(2, userId);
                    updatePs.setInt(3, itemId);
                    int affectedRows = updatePs.executeUpdate();

                    if (affectedRows > 0) {
                        con.commit(); // Commit transaction
                        System.out.println("Bid update successful.");
                        return true;
                    } else {
                        System.out.println("No rows updated, bid not high enough or condition failed.");
                    }
                } else {
                    System.out.println("New bid is not sufficient to update (less than current bid + increment).");
                }
            } else {
                System.out.println("No item found with the given ID that is active.");
            }
            con.rollback(); // Rollback transaction if bid not higher or no rows affected
            return false;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.out.println("SQL Exception on rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (updatePs != null) updatePs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception during resource cleanup: " + e.getMessage());
            }
        }
    }


}
