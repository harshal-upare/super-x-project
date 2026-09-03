package com.desgin.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationModel {

    private String notificationId;
    private String userId;
    private String title;
    private String message;
    private String type; // "BOOKING", "PAYMENT", "STATUS", "SYSTEM"
    private String relatedId;
    private boolean read;
    private String createdAt;
    private long timestamp;

    public NotificationModel() {
        this.read = false;
        this.timestamp = System.currentTimeMillis();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"));
    }

    public NotificationModel(String notificationId, String userId, String title, String message, String type, String relatedId) {
        this.notificationId = notificationId != null ? notificationId : "NOTIF_" + System.currentTimeMillis();
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type != null ? type : "SYSTEM";
        this.relatedId = relatedId;
        this.read = false;
        this.timestamp = System.currentTimeMillis();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"));
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
