package com.cs336.dao;

import com.cs336.dao.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.cs336.dao.Bid;

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
    public List<Item> searchItems(String query, String sortBy, String sortOrder) throws SQLException {
        List<Item> items = new ArrayList<>();
        // Build SQL query string with dynamic sorting
        String sql = "SELECT * FROM items WHERE (title LIKE ? OR description LIKE ? OR item_type LIKE ?) AND closing_time > NOW() AND status = 'active' ORDER BY " + sortBy + " " + sortOrder;
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ps.setString(3, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
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
                    item.setBidIncrement(rs.getDouble("bid_increment"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Bid> getBidHistory(int itemId) throws SQLException {
        List<Bid> bidHistory = new ArrayList<>();
        String sql = "SELECT * FROM bids WHERE item_id = ? ORDER BY bid_time DESC";
        try (Connection con = db.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bid bid = new Bid(
                        rs.getInt("bid_id"),
                        rs.getInt("item_id"),
                        rs.getInt("user_id"),
                        rs.getString("bid_type"),
                        rs.getDouble("bid_amount"),
                        rs.getDouble("auto_increment"),
                        rs.getDouble("auto_limit"),
                        rs.getTimestamp("bid_time")
                    );
                    bidHistory.add(bid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bidHistory;
    }


    public List<Item> getUserAuctions(int userId) throws SQLException {
        List<Item> auctions = new ArrayList<>();
        String sql = "SELECT DISTINCT items.* FROM items JOIN bids ON items.item_id = bids.item_id WHERE bids.user_id = ? OR items.seller_id = ? ORDER BY items.closing_time DESC";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
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
                    item.setBidIncrement(rs.getDouble("bid_increment"));
                    auctions.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }


    public void checkAndCloseBids() {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement("SELECT item_id, seller_id, current_bid, minimum_price, current_bid_user_id FROM items WHERE closing_time <= NOW() AND status = 'active' FOR UPDATE")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    double currentBid = rs.getDouble("current_bid");
                    double minimumPrice = rs.getDouble("minimum_price");
                    int sellerId = rs.getInt("seller_id");
                    int currentBidUserId = rs.getInt("current_bid_user_id");

                    System.out.println("Processing item: " + itemId + " | Current Bid: " + currentBid + " | Minimum Price: " + minimumPrice);

                    if (currentBid >= minimumPrice) {
                        try (PreparedStatement updateStmt = con.prepareStatement("UPDATE items SET status = 'sold' WHERE item_id = ?")) {
                            updateStmt.setInt(1, itemId);
                            updateStmt.executeUpdate();
                            new AlertDAO().createAlert(currentBidUserId, "Congratulations! You have won the auction for item ID: " + itemId, itemId, "win_notification");
                        }
                    } else {
                        try (PreparedStatement updateStmt = con.prepareStatement("UPDATE items SET status = 'removed' WHERE item_id = ?")) {
                            updateStmt.setInt(1, itemId);
                            updateStmt.executeUpdate();
                            new AlertDAO().createAlert(sellerId, "The auction closed, but the reserve price was not met for item ID: " + itemId, itemId, "item_removed");
                        }
                    }
                }
                con.commit();
            } catch (SQLException e) {
                System.err.println("Failed to process items: " + e.getMessage());
                con.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Database connection issue: " + e.getMessage());
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
    public List<Bid> getUserBids(int userId) throws SQLException {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM bids WHERE user_id = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bid bid = new Bid(
                        rs.getInt("bid_id"),
                        rs.getInt("item_id"),
                        userId,
                        rs.getString("bid_type"),
                        rs.getDouble("bid_amount"),
                        rs.getDouble("auto_increment"),
                        rs.getDouble("auto_limit"),
                        rs.getTimestamp("bid_time")
                    );
                    bids.add(bid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions or throw them to be caught by the calling servlet
        }
        return bids;
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
