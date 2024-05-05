package com.cs336.servlet;
import com.cs336.dao.ApplicationDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private ApplicationDB db = new ApplicationDB(); // Ensure this is correctly initialized.

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("createRep".equals(action)) {
                createRepAccount(request, response);
            } else if ("generateReport".equals(action)) {
                generateSalesReport(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the error instead of printing stack trace
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            e.printStackTrace(); // Consider logging the error instead of printing stack trace
            response.sendRedirect("error.jsp");
        }
    }

    private void createRepAccount(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'customer_rep')");
            ps.setString(1, username);
            ps.setString(2, password);
            int result = ps.executeUpdate();
            if (result > 0) {
                request.getSession().setAttribute("successMessage", "Account for '" + username + "' created successfully.");
                response.sendRedirect("admin_dashboard.jsp");
            } else {
                throw new SQLException("Failed to create a new account.");
            }
        }
    }

    private void generateSalesReport(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String reportType = request.getParameter("reportType");
        GeneratingReport report = new GeneratingReport();
        Object reportData; // To handle different types of data from reports

        switch (reportType) {
            case "totalEarnings":
                reportData = report.getTotalEarnings();
                break;
            case "earningsPerItem":
                reportData = report.getEarningsPerItem();
                break;
            case "earningsPerItemType":
                reportData = report.getEarningsPerItemType();
                break;
            case "earningsPerUser":
                reportData = report.getEarningsPerUser();
                break;
            case "bestSellingItems":
                reportData = report.getBestSellingItems();
                break;
            case "bestBuyers":
                reportData = report.getBestBuyers();
                break;
            default:
                reportData = "Invalid report type selected";
                break;
        }

        request.setAttribute("reportType", reportType); // Pass the report type
        request.setAttribute("reportData", reportData); // Pass the actual data
        System.out.println(reportData + reportType);
        request.getRequestDispatcher("report_result.jsp").forward(request, response);
    }
}
