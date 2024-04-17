package com.cs336.utils;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    public static void logInUser(HttpSession session, String username, int userId) {
        if (session != null) {
            session.setAttribute("user", username);
            session.setAttribute("userID", userId);  // Storing the userID in session
        }
    }

    public static void logOutUser(HttpSession session) {
        if (session != null) {
            session.invalidate();  // Invalidate the session, clearing all data
        }
    }

    public static String getLoggedInUser(HttpSession session) {
        return (session != null) ? (String) session.getAttribute("user") : null;
    }

    public static Integer getLoggedInUserID(HttpSession session) {
        return (session != null) ? (Integer) session.getAttribute("userID") : null;
    }
}
