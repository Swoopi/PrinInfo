package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.cs336.dao.AlertDAO;


@WebServlet("/ListItemServlet")
public class ListItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));
        double bidIncrement = Double.parseDouble(request.getParameter("bidIncrement"));
        double minimumPrice = Double.parseDouble(request.getParameter("minimumPrice"));
        String itemType = request.getParameter("itemType");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Timestamp closingTime = null;
        try {
            Date parsedDate = dateFormat.parse(request.getParameter("closingTime"));
            closingTime = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Error parsing the closing time. Please use the correct format.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        ApplicationDB db = new ApplicationDB();
        try (Connection con = db.getConnection()) {
            String query = "INSERT INTO items (seller_id, title, description, starting_price, bid_increment, minimum_price, closing_time, status, starting_time, item_type) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, 'active', NOW(), ?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            HttpSession session = request.getSession();
            Integer userID = (Integer) session.getAttribute("userID");
            if(userID == null) {
                response.sendRedirect("landing.jsp");
                return;
            }

            ps.setInt(1, userID);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setDouble(4, startingPrice);
            ps.setDouble(5, bidIncrement);
            ps.setDouble(6, minimumPrice);
            ps.setTimestamp(7, closingTime);
            ps.setString(8, itemType);
            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int itemId = rs.getInt(1);
                    AlertDAO alertDAO = new AlertDAO();
                    alertDAO.createAlert(userID, "You have successfully listed the item: " + title, itemId, "item_listed");
                }
                response.sendRedirect("CurrentItemsServlet");
            } else {
                request.setAttribute("errorMessage", "Failed to list item. Please try again.");
                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
