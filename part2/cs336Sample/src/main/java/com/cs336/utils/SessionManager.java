package com.cs336.utils;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static void logInUser(HttpSession session, String username, int userId) {
        if (session != null) {
            session.setAttribute("username", username);
            session.setAttribute("userID", userId);  // Ensure this is consistent
            System.out.println("Logged in user: " + username + " with userID: " + userId);
        }
    }

    public static void logOutUser(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    public static String getLoggedInUser(HttpSession session) {
        return (session != null) ? (String) session.getAttribute("username") : null;
    }

    public static Integer getLoggedInUserID(HttpSession session) {
        return (session != null) ? (Integer) session.getAttribute("userID") : null;
    }
    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userID") != null;
    }
}
