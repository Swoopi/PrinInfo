package com.cs336.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import com.cs336.dao.ApplicationDB;
import com.cs336.dao.QueryDAO;
import com.cs336.dao.Query;

@WebServlet("/QueryManagerServlet")
public class QueryManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch open queries from the database
        QueryDAO queryDAO = new QueryDAO();
        request.setAttribute("queries", queryDAO.getOpenQueries());
        RequestDispatcher dispatcher = request.getRequestDispatcher("manageQueries.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle actions
        String action = request.getParameter("action");
        if ("respondQuery".equals(action)) {
            respondToQuery(request, response);
        }
    }

    private void respondToQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int queryId = Integer.parseInt(request.getParameter("queryId"));
        String responseText = request.getParameter("response");
        QueryDAO queryDAO = new QueryDAO();

        if (queryDAO.updateQueryResponse(queryId, responseText)) {
            response.sendRedirect("manageQueries.jsp");  // Redirect to refresh the page/list
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to process query response.");
        }
    }
}
