package com.desgin.model;

public class RentalRequestModel {

    private String requestId;
    private String machineryId;
    private String machineryName;
    private String category;
    private int dailyRate;
    private int days;
    private int totalAmount;
    private String startDate;
    private String endDate;
    private String farmerEmail;
    private String farmerName;
    private String farmerPhone;
    private String farmerLocation;
    private String providerEmail;
    private String providerName;
    private String providerPhone;
    private String providerLocation;
    private String deliveryMode;
    private String status; // "PENDING", "APPROVED", "DECLINED", "COMPLETED", "CANCELLED"
    private String paymentStatus; // "ESCROW HELD", "SETTLED", "REFUNDED", "PENDING"
    private String createdAt;
    private String imagePath;
    private String district;
    private long shiftStartTime;
    private long shiftDurationMillis;

    public RentalRequestModel() {
        this.status = "PENDING";
        this.paymentStatus = "ESCROW HELD";
        this.deliveryMode = "Pickup / Delivery";
    }

    public RentalRequestModel(String requestId, String machineryId, String machineryName, String category,
                              int dailyRate, int days, int totalAmount, String startDate, String endDate,
                              String farmerEmail, String farmerName, String farmerPhone, String farmerLocation,
                              String providerEmail, String providerName, String providerPhone, String providerLocation,
                              String deliveryMode, String imagePath) {
        this.requestId = requestId;
        this.machineryId = machineryId;
        this.machineryName = machineryName;
        this.category = category;
        this.dailyRate = dailyRate;
        this.days = days;
        this.totalAmount = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.farmerEmail = farmerEmail;
        this.farmerName = farmerName;
        this.farmerPhone = farmerPhone;
        this.farmerLocation = farmerLocation;
        this.providerEmail = providerEmail;
        this.providerName = providerName;
        this.providerPhone = providerPhone;
        this.providerLocation = providerLocation;
        this.deliveryMode = (deliveryMode != null && !deliveryMode.isEmpty()) ? deliveryMode : "Pickup / Delivery";
        this.status = "PENDING";
        this.paymentStatus = "ESCROW HELD";
        this.createdAt = java.time.LocalDate.now().toString();
        this.imagePath = imagePath;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMachineryId() {
        return machineryId;
    }

    public void setMachineryId(String machineryId) {
        this.machineryId = machineryId;
    }

    public String getMachineryName() {
        return machineryName;
    }

    public void setMachineryName(String machineryName) {
        this.machineryName = machineryName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(int dailyRate) {
        this.dailyRate = dailyRate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerPhone() {
        return farmerPhone;
    }

    public void setFarmerPhone(String farmerPhone) {
        this.farmerPhone = farmerPhone;
    }

    public String getFarmerLocation() {
        return farmerLocation;
    }

    public void setFarmerLocation(String farmerLocation) {
        this.farmerLocation = farmerLocation;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public String getProviderLocation() {
        return providerLocation;
    }

    public void setProviderLocation(String providerLocation) {
        this.providerLocation = providerLocation;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDistrict() {
        return district != null ? district : providerLocation;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    private String farmerProfilePic;
    private String paymentMode; // "Razorpay"
    private String paymentTransactionId;
    private int amountPaid;
    private String providerBankName;
    private String providerAccountNumber;
    private String providerIfsc;
    private String providerUpiId;

    public String getFarmerProfilePic() {
        return farmerProfilePic;
    }

    public void setFarmerProfilePic(String farmerProfilePic) {
        this.farmerProfilePic = farmerProfilePic;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getProviderBankName() {
        return providerBankName;
    }

    public void setProviderBankName(String providerBankName) {
        this.providerBankName = providerBankName;
    }

    public String getProviderAccountNumber() {
        return providerAccountNumber;
    }

    public void setProviderAccountNumber(String providerAccountNumber) {
        this.providerAccountNumber = providerAccountNumber;
    }

    public String getProviderIfsc() {
        return providerIfsc;
    }

    public void setProviderIfsc(String providerIfsc) {
        this.providerIfsc = providerIfsc;
    }

    public String getProviderUpiId() {
        return providerUpiId;
    }

    public void setProviderUpiId(String providerUpiId) {
        this.providerUpiId = providerUpiId;
    }

    private boolean operatorRequired;
    private String operatorId;
    private String operatorName;
    private String operatorPhone;
    private String operatorStatus; // "PENDING", "ACCEPTED", "REJECTED"
    private int equipmentAmount;
    private int operatorAmount;
    private String updatedAt;
    private String rejectionReason;

    public boolean isOperatorRequired() {
        return operatorRequired;
    }

    public void setOperatorRequired(boolean operatorRequired) {
        this.operatorRequired = operatorRequired;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public String getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(String operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public int getEquipmentAmount() {
        return equipmentAmount;
    }

    public void setEquipmentAmount(int equipmentAmount) {
        this.equipmentAmount = equipmentAmount;
    }

    public int getOperatorAmount() {
        return operatorAmount;
    }

    public void setOperatorAmount(int operatorAmount) {
        this.operatorAmount = operatorAmount;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public long getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(long shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public long getShiftDurationMillis() {
        return shiftDurationMillis;
    }

    public void setShiftDurationMillis(long shiftDurationMillis) {
        this.shiftDurationMillis = shiftDurationMillis;
    }
}
