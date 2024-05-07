package com.cs336.dao;

import com.cs336.dao.Alert;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {
    private ApplicationDB db;

    public AlertDAO() {
        this.db = new ApplicationDB();
    }

    public List<Alert> getAlertsForUser(int userId) {
        List<Alert> alerts = new ArrayList<>();
        String query = "SELECT * FROM alerts WHERE user_id = ? AND is_read = 0 ORDER BY created_at DESC";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                alerts.add(new Alert(
                    rs.getInt("alert_id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getInt("related_item_id"),
                    rs.getString("alert_type"),
                    rs.getBoolean("is_read"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return alerts;
    }

    public boolean markAlertAsRead(int alertId) {
        String query = "UPDATE alerts SET is_read = 1 WHERE alert_id = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, alertId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Additional methods can be implemented here similar to ItemDAO
}
