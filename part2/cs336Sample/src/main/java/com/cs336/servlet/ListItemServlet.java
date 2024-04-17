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


@WebServlet("/ListItemServlet")
public class ListItemServlet extends HttpServlet {

	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));
        double bidIncrement = Double.parseDouble(request.getParameter("bidIncrement"));
        double minimumPrice = Double.parseDouble(request.getParameter("minimumPrice"));


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Timestamp closingTime = null;

        try {
            // Parse the string into a proper Timestamp object, assuming the parameter includes AM or PM
            Date parsedDate = dateFormat.parse(request.getParameter("closingTime"));
            closingTime = new Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            // Handle the parse exception by setting an error message and forwarding to an error page
            request.setAttribute("errorMessage", "Error parsing the closing time. Please use the correct format, including AM or PM.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        ApplicationDB db = new ApplicationDB();
        try (Connection con = db.getConnection()) {
            String query = "INSERT INTO Items (seller_id, title, description, starting_price, bid_increment, minimum_price, closing_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

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
            ps.executeUpdate();

            response.sendRedirect("currentItems.jsp");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
