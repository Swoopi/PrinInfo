package com.cs336.dao;

import java.sql.Timestamp;

public class Alert {
    private int alertId;
    private int userId;
    private String message;
    private int relatedItemId;
    private String alertType;
    private boolean isRead;
    private Timestamp createdAt;

    // Constructor
    public Alert(int alertId, int userId, String message, int relatedItemId, String alertType, boolean isRead, Timestamp createdAt) {
        this.alertId = alertId;
        this.userId = userId;
        this.message = message;
        this.relatedItemId = relatedItemId;
        this.alertType = alertType;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRelatedItemId() {
        return relatedItemId;
    }

    public void setRelatedItemId(int relatedItemId) {
        this.relatedItemId = relatedItemId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
