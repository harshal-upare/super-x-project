package com.desgin.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SupportQueryModel {

    private String queryId;
    private String userEmail;
    private String userName;
    private String userRole; // e.g. "Operator"
    private String userPhone;
    private String subject;
    private String status; // "OPEN" or "RESOLVED"
    private String lastMessage;
    private long lastUpdated;
    private String createdAt;
    private List<SupportMessageModel> messages;
    private int feedbackRating; // 1-5
    private String feedbackComment;
    private boolean feedbackGiven;

    public SupportQueryModel() {
        this.messages = new ArrayList<>();
        this.status = "OPEN";
        this.lastUpdated = System.currentTimeMillis();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        this.feedbackRating = 0;
        this.feedbackComment = "";
        this.feedbackGiven = false;
    }

    public SupportQueryModel(String queryId, String userEmail, String userName, String userRole, String userPhone, String subject) {
        this.queryId = queryId != null ? queryId : "QRY_" + System.currentTimeMillis();
        this.userEmail = userEmail;
        this.userName = userName;
        this.userRole = userRole != null ? userRole : "Operator";
        this.userPhone = userPhone != null ? userPhone : "";
        this.subject = subject != null ? subject : "General Support Inquiry";
        this.status = "OPEN";
        this.lastMessage = "";
        this.lastUpdated = System.currentTimeMillis();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        this.messages = new ArrayList<>();
        this.feedbackRating = 0;
        this.feedbackComment = "";
        this.feedbackGiven = false;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<SupportMessageModel> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public void setMessages(List<SupportMessageModel> messages) {
        this.messages = messages;
    }

    public int getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(int feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public boolean isFeedbackGiven() {
        return feedbackGiven;
    }

    public void setFeedbackGiven(boolean feedbackGiven) {
        this.feedbackGiven = feedbackGiven;
    }
}
