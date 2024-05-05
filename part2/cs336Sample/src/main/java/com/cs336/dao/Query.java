package com.cs336.dao;

public class Query {
    private int queryId;
    private int userId;
    private String content;
    private String response;
    private String status;

    public Query(int queryId, int userId, String content, String response, String status) {
        this.queryId = queryId;
        this.userId = userId;
        this.content = content;
        this.response = response;
        this.status = status;
    }

    // Getters and setters
    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
