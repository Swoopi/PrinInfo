package com.cs336.dao;

import com.cs336.dao.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryDAO {
    private ApplicationDB db;

    public QueryDAO() {
        this.db = new ApplicationDB();
    }

    public List<Query> getOpenQueries() {
        List<Query> queries = new ArrayList<>();
        String sql = "SELECT * FROM user_queries WHERE status = 'open'";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                queries.add(new Query(
                    rs.getInt("query_id"),
                    rs.getInt("user_id"),
                    rs.getString("content"),
                    rs.getString("response"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queries;
    }

    public boolean updateQueryResponse(int queryId, String response) {
        String sql = "UPDATE user_queries SET response = ?, status = 'closed' WHERE query_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, response);
            ps.setInt(2, queryId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
