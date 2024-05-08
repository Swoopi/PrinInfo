package com.cs336.servlet;
import com.cs336.dao.ApplicationDB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import com.cs336.dao.ItemDAO;
import com.cs336.dao.Item;
import java.util.List;


@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String searchType = request.getParameter("searchType");
	    String query = request.getParameter("query");
	    String sortBy = request.getParameter("sortBy");
	    String sortOrder = request.getParameter("sortOrder"); // Expect 'ASC' or 'DESC'

	    ItemDAO itemDAO = new ItemDAO();
	    HttpSession session = request.getSession();

	    try {
	        if (searchType.equals("itemSearch")) {
	            List<Item> items;
	            if (query == null || query.trim().isEmpty()) {
	                items = itemDAO.getActiveItems(); // Fetch all active items if query is empty
	            } else {
	                items = itemDAO.searchItems(query, sortBy, sortOrder);
	            }
	            session.setAttribute("searchResults", items);
	        } else if (searchType.equals("bidHistory")) {
	            session.setAttribute("searchResults", itemDAO.getBidHistory(Integer.parseInt(query)));
	        } else if (searchType.equals("userAuctions")) {
	            session.setAttribute("searchResults", itemDAO.getUserAuctions(Integer.parseInt(query)));
	        }
	        response.sendRedirect("search.jsp");
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("errorMessage", "Error during search: " + e.getMessage());
	        request.getRequestDispatcher("errorPage.jsp").forward(request, response);
	    }
	}
}
