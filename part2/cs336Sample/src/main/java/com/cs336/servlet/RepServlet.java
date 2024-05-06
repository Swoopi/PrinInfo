package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import com.cs336.dao.Item;
import com.cs336.dao.Bid;
import com.cs336.dao.Query;
import com.cs336.dao.User;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RepServlet")
public class RepServlet extends HttpServlet {
    private ApplicationDB db = new ApplicationDB(); // Ensure this is correctly initialized.

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "manageQueries":
                    manageQueries(request, response);
                    break;
                case "listUsers":
                    listUsers(request, response);
                    break;
                case "editUser":
                    editUser(request, response);
                    break;
                case "deleteUser":
                    deleteUser(request, response);
                    break;
                case "manageBids":
                    manageBids(request, response);
                    break;
                case "removeAuctions":
                    removeAuctions(request, response);
                    break;
                default:
                    response.sendRedirect("rep_dashboard.jsp");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT userid, username, role FROM users";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                    rs.getInt("userid"),
                    rs.getString("username"),
                    rs.getString("role")
                );
                users.add(user);
            }
            request.setAttribute("users", users);
            RequestDispatcher dispatcher = request.getRequestDispatcher("editUsers.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int userId = Integer.parseInt(request.getParameter("userid"));
        String sql = "DELETE FROM users WHERE userid = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int result = ps.executeUpdate();
            if (result > 0) {
                // User deleted successfully
                System.out.println("User deleted successfully");
                response.sendRedirect("rep_dashboard.jsp");  // Redirect to the dashboard
            } else {
                // No record was deleted
                System.out.println("Failed to delete user");
                request.setAttribute("errorMessage", "Failed to delete user.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error while deleting user: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }






    private void manageQueries(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Query> queries = new ArrayList<>();
        String sql = "SELECT * FROM user_queries WHERE status = 'open'";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Query query = new Query(
                    rs.getInt("query_id"),
                    rs.getInt("user_id"),
                    rs.getString("content"),
                    rs.getString("response"),
                    rs.getString("status")
                );
                queries.add(query);
            }
            request.setAttribute("queries", queries);
            RequestDispatcher dispatcher = request.getRequestDispatcher("manageQueries.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
    	String userIdStr = request.getParameter("userid");
    	int userId = Integer.parseInt(userIdStr);  // This line throws the exception if userIdStr is null
        String sql = "SELECT * FROM users WHERE userid = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getInt("userid"), rs.getString("username"), rs.getString("role"));
                request.setAttribute("user", user);
                RequestDispatcher dispatcher = request.getRequestDispatcher("editUserForm.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "User not found.");
                response.sendRedirect("RepServlet?action=editUsers");
            }
        }
    }


    private void manageBids(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT bid_id, item_id, user_id, bid_amount FROM bids ORDER BY bid_amount DESC";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bid bid = new Bid(
                    rs.getInt("bid_id"),
                    rs.getInt("item_id"),
                    rs.getInt("user_id"),
                    rs.getDouble("bid_amount")
                );
                bids.add(bid);
            }
            request.setAttribute("bids", bids);
            RequestDispatcher dispatcher = request.getRequestDispatcher("manageBids.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void removeAuctions(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
    	    List<Item> items = new ArrayList<>();
    	    String sql = "SELECT * FROM items WHERE status = 'active'"; // Example SQL
    	    try (Connection con = db.getConnection();
    	         PreparedStatement ps = con.prepareStatement(sql);
    	         ResultSet rs = ps.executeQuery()) {
    	        while (rs.next()) {
    	        	Item item = new Item(
    	                    rs.getInt("item_id"),
    	                    rs.getInt("seller_id"), // Assuming the column name is seller_id in your database
    	                    rs.getString("title"),
    	                    rs.getString("description"),
    	                    rs.getDouble("starting_price"),
    	                    rs.getDouble("current_bid"),
    	                    rs.getInt("current_bid_user_id"),
    	                    rs.getTimestamp("starting_time"),
    	                    rs.getTimestamp("closing_time"),
    	                    rs.getString("status")
    	                );
    	            items.add(item);
    	        }
    	        request.setAttribute("items", items);
    	        RequestDispatcher dispatcher = request.getRequestDispatcher("removeAuctions.jsp");
    	        dispatcher.forward(request, response);
    	    }
    	}
    }
