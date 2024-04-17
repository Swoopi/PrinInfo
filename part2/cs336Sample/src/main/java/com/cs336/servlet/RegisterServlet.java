package com.cs336.servlet;

import com.cs336.dao.ApplicationDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Remember to hash and salt passwords in real applications
        boolean userExists = false;

        ApplicationDB db = new ApplicationDB();

        try (Connection conn = db.getConnection();
             PreparedStatement checkUser = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
             PreparedStatement insertUser = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            checkUser.setString(1, username);
            try (ResultSet rs = checkUser.executeQuery()) {
                userExists = rs.next();
            }

            if (!userExists) {
                insertUser.setString(1, username);
                insertUser.setString(2, password);
                int result = insertUser.executeUpdate();

                if (result > 0) {
                    out.println("<h2>Registration successful!</h2>");
                    response.setHeader("Refresh", "5; URL=landing.jsp");
                    out.println("<p>You will be redirected to the login page in 5 seconds.</p>");
                    out.println("<p>Click <a href='landing.jsp'>here</a> if you are not redirected.</p>");
                } else {
                    out.println("<h2>Registration failed. Please try again.</h2>");
                    response.setHeader("Refresh", "5; URL=register.jsp");
                    out.println("<p>You will be redirected back to the registration page in 5 seconds.</p>");
                    out.println("<p>Click <a href='register.jsp'>here</a> if you are not redirected.</p>");
                }
            } else {
                out.println("<h2>Username already exists. Please choose a different one.</h2>");
                response.setHeader("Refresh", "5; URL=register.jsp");
                out.println("<p>You will be redirected back to the registration page in 5 seconds.</p>");
                out.println("<p>Click <a href='register.jsp'>here</a> if you are not redirected.</p>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Error during the registration process.</h2>");
            // Optional: Redirect to an error page
        }
    }
}
