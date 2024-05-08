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
        PreparedStatement ps = null, updateStmt = null;
        ResultSet rs = null;
        AlertDAO alertDAO = new AlertDAO();

        try {
            con = db.getConnection();
            con.setAutoCommit(false); // Start transaction for batch update

            // Fetch all active items that are past their closing time
            String fetchItemsQuery = "SELECT item_id, seller_id, current_bid, minimum_price, current_bid_user_id FROM items WHERE closing_time <= NOW() AND status = 'active'";
            ps = con.prepareStatement(fetchItemsQuery);
            rs = ps.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                double currentBid = rs.getDouble("current_bid");
                double minimumPrice = rs.getDouble("minimum_price");
                int currentBidUserId = rs.getInt("current_bid_user_id");
                int sellerId = rs.getInt("seller_id");
                System.out.println(currentBid + "CB  MP " + minimumPrice);
                if (currentBid >= minimumPrice) {
                    // Item sold
                    String updateSoldQuery = "UPDATE items SET status = 'sold' WHERE item_id = ?";
                    updateStmt = con.prepareStatement(updateSoldQuery);
                    updateStmt.setInt(1, itemId);
                    updateStmt.executeUpdate();

                    // Alert the winner
                    if (currentBidUserId != 0) {
                        alertDAO.createAlert(currentBidUserId, "Congratulations! You have won the auction for item ID: " + itemId, itemId, "win_notification");
                    }
                } else {
                    // Reserve not met, item removed
                    String updateRemovedQuery = "UPDATE items SET status = 'removed' WHERE item_id = ?";
                    updateStmt = con.prepareStatement(updateRemovedQuery);
                    updateStmt.setInt(1, itemId);
                    updateStmt.executeUpdate();

                    // Alert the seller that the reserve was not met
                    alertDAO.createAlert(sellerId, "The auction closed, but the reserve price was not met for item ID: " + itemId, itemId, "item_removed");
                }
            }

            con.commit(); // Commit all changes
            System.out.println("Transactions committed successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            try {
                if (con != null) {
                    con.rollback(); // Rollback transaction if any error occurs
                    System.out.println("Transaction rolled back.");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception on rollback: " + ex.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                if (updateStmt != null) updateStmt.close();
                if (ps != null) ps.close();
                if (rs != null) rs.close();
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
    public int updateBid(int itemId, int userId, double bidAmount) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement updatePs = null;
        ResultSet rs = null;
        int previousBidderId = -1;

        try {
            con = db.getConnection();
            con.setAutoCommit(false); // Start transaction

            // Prepare a query to fetch the current bid and bid increment
            String query = "SELECT current_bid, bid_increment, current_bid_user_id FROM items WHERE item_id = ? AND status = 'active'";
            ps = con.prepareStatement(query);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();

            if (rs.next()) {
                double currentBid = rs.getDouble("current_bid");
                double bidIncrement = rs.getDouble("bid_increment");
                previousBidderId = rs.getInt("current_bid_user_id");  // Get the ID of the current highest bidder

                if (bidAmount >= (currentBid + bidIncrement)) {
                    String updateQuery = "UPDATE items SET current_bid = ?, current_bid_user_id = ? WHERE item_id = ?";
                    updatePs = con.prepareStatement(updateQuery);
                    updatePs.setDouble(1, bidAmount);
                    updatePs.setInt(2, userId);
                    updatePs.setInt(3, itemId);
                    int affectedRows = updatePs.executeUpdate();

                    if (affectedRows > 0) {
                    	AlertDAO alertDAO = new AlertDAO();
                    	String message = "You have been outbit on item ID:  " + itemId;
                    }
                    con.commit();  // Commit the transaction
                    return previousBidderId;  // Return the previous bidder's ID
                }
                
                con.rollback();  
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            try {
                con.rollback();  // Rollback transaction if there's an error
            } catch (SQLException ex) {
                System.out.println("SQL Exception on rollback: " + ex.getMessage());
            }
            e.printStackTrace();
        } finally {
            closeResources(con, rs, ps, updatePs);  // Properly close all resources
        }
        return -1;  // Return -1 to indicate failure or no previous bidder
    }


    private void closeResources(Connection con, ResultSet rs, Statement... statements) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (statements != null) {
                for (Statement stmt : statements) {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception during resource cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
