package com.desgin.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentModel {

    private String paymentId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String bookingId;
    private String farmerId;
    private String farmerName;
    private String providerId;
    private String providerName;
    private String operatorId;
    private int amount;
    private String currency;
    private String paymentStatus; // "PAID", "PENDING", "FAILED", "REFUNDED"
    private String paymentMethod; // "Razorpay Online"
    private String createdAt;
    private String verifiedAt;

    public PaymentModel() {
        this.currency = "INR";
        this.paymentStatus = "PAID";
        this.paymentMethod = "Razorpay Online";
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        this.verifiedAt = this.createdAt;
    }

    public PaymentModel(String paymentId, String razorpayOrderId, String razorpayPaymentId,
                        String bookingId, String farmerId, String farmerName,
                        String providerId, String providerName, String operatorId,
                        int amount) {
        this.paymentId = paymentId != null ? paymentId : "PAY_" + System.currentTimeMillis();
        this.razorpayOrderId = razorpayOrderId;
        this.razorpayPaymentId = razorpayPaymentId;
        this.bookingId = bookingId;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.operatorId = operatorId;
        this.amount = amount;
        this.currency = "INR";
        this.paymentStatus = "PAID";
        this.paymentMethod = "Razorpay Online";
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        this.verifiedAt = this.createdAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}
