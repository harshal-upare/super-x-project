package com.desgin.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PayoutModel {

    private String payoutId;
    private String userId;
    private String userRole; // "PROVIDER", "OPERATOR"
    private String bookingId;
    private String paymentId;
    private int amount;
    private String status; // "PENDING", "PROCESSING", "PAID", "FAILED"
    private String payoutDate;
    private String transactionReference;
    private String bankDetails;
    private String createdAt;
    private String updatedAt;

    public PayoutModel() {
        this.status = "PAID";
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        this.updatedAt = this.createdAt;
        this.payoutDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public PayoutModel(String payoutId, String userId, String userRole, String bookingId,
                       String paymentId, int amount, String status, String transactionReference,
                       String bankDetails) {
        this.payoutId = payoutId != null ? payoutId : "PO_" + System.currentTimeMillis();
        this.userId = userId;
        this.userRole = userRole;
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status != null ? status : "PAID";
        this.transactionReference = transactionReference;
        this.bankDetails = bankDetails;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        this.updatedAt = this.createdAt;
        this.payoutDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(String payoutId) {
        this.payoutId = payoutId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayoutDate() {
        return payoutDate;
    }

    public void setPayoutDate(String payoutDate) {
        this.payoutDate = payoutDate;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
