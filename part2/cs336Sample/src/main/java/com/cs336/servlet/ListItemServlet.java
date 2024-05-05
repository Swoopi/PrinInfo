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
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String collection = request.getParameter("collection");
        String brand = request.getParameter("brand");
        String f = request.getParameter("ft"); 
        String fv = request.getParameter("ftval");
        String[] features = f.split(","); 
        String[] featureVal = fv.split(",");
        int manYear = Integer.parseInt(request.getParameter("myr"));
        int purYear = Integer.parseInt(request.getParameter("pyr"));
        double msrp = Double.parseDouble(request.getParameter("msrp"));
        double startingPrice = Double.parseDouble(request.getParameter("startingPrice"));
        double bidIncrement = Double.parseDouble(request.getParameter("bidIncrement"));
        double minimumPrice = Double.parseDouble(request.getParameter("minimumPrice"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Timestamp closingTime = null;

        try {
            Date parsedDate = dateFormat.parse(request.getParameter("closingTime"));
            closingTime = new Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Error parsing the closing time. Please use the correct format, including AM or PM.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        ApplicationDB db = new ApplicationDB();
        try (Connection con = db.getConnection()) {
            String query = "INSERT INTO Auctions (sellerID,  description, starting_price, bid_increment, minimum_price, closing_time, auction_status, starting_time) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, 'active', NOW())";
            PreparedStatement ps = con.prepareStatement(query);
            
            String itemQuery = "INSERT INTO Items (item_name, brand, year_manufactured, category, collection, msrp, purchase_year) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement itemPS = con.prepareStatement(itemQuery);

            
            String featQuery = "INSERT INTO ItemFeatures (item_name, feature_desc, feature_value) "
            		+ "VALUES (?, ?, ?)";
            PreparedStatement featPS = con.prepareStatement(featQuery);
            
            HttpSession session = request.getSession();
            Integer userID = (Integer) session.getAttribute("userID");
            if(userID == null) {
                response.sendRedirect("landing.jsp");
                return;
            }

            con.setAutoCommit(false);
            ps.setInt(1, userID);
            ps.setString(2, name);
            ps.setDouble(3, startingPrice);
            ps.setDouble(4, bidIncrement);
            ps.setDouble(5, minimumPrice);
            ps.setTimestamp(6, closingTime);
            ps.executeUpdate();
            con.commit();
            
            itemPS.setString(1, name);
            itemPS.setString(2, brand);
            itemPS.setInt(3, manYear);
            itemPS.setString(4, category);
            itemPS.setString(5, collection);
            itemPS.setDouble(6, msrp);
            itemPS.setInt(7, purYear);
            itemPS.executeUpdate();
            con.commit();
;            
            for(int i = 0; i<features.length; i++) {
            	featPS.setString(1, name);
            	featPS.setString(2, features[i]);
            	featPS.setString(3, featureVal[i]);
            	featPS.executeUpdate();
            	con.commit();
            }
            
            
            itemPS.executeUpdate();
            
            
            /*
            for(int i = 0; i<features.length; i++) {
            	featPS.setString(1, name);
            	featPS.setString(2, features[i]);
            	featPS.setString(3, features[i]);
            	featPS.executeUpdate();
            }
            */
            response.sendRedirect("CurrentItemsServlet");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
