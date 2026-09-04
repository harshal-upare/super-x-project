package com.desgin.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SupportMessageModel {

    private String messageId;
    private String senderEmail;
    private String senderName;
    private String senderRole; // "Operator" or "Admin"
    private String text;
    private long timestamp;
    private String formattedTime;

    public SupportMessageModel() {
        this.timestamp = System.currentTimeMillis();
        this.formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public SupportMessageModel(String messageId, String senderEmail, String senderName, String senderRole, String text) {
        this.messageId = messageId != null ? messageId : "MSG_" + System.currentTimeMillis();
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
        this.formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }
}
