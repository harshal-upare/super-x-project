package com.desgin.model;

public class MachineryModel {

    private String id;
    private String name;
    private String category;
    private int pricePerDay;
    private String location;
    private String district;
    private String state;
    private String providerEmail;
    private String providerName;
    private String providerPhone;
    private boolean hasOperator;
    private String status; // "AVAILABLE", "RENTED OUT", "IN SERVICE"
    private String imagePath;
    private String rating;
    private String specs;
    private String createdAt;

    // No-arg constructor required by Firestore
    public MachineryModel() {
        this.status = "AVAILABLE";
        this.rating = "4.8";
    }

    public MachineryModel(String id, String name, String category, int pricePerDay, String location,
                          String providerEmail, String providerName, String providerPhone,
                          boolean hasOperator, String imagePath, String specs) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.location = location;
        this.providerEmail = providerEmail;
        this.providerName = providerName;
        this.providerPhone = providerPhone;
        this.hasOperator = hasOperator;
        this.imagePath = imagePath;
        this.specs = specs;
        this.status = "AVAILABLE";
        this.rating = "4.8";
        this.createdAt = java.time.LocalDate.now().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public boolean isHasOperator() {
        return hasOperator;
    }

    public void setHasOperator(boolean hasOperator) {
        this.hasOperator = hasOperator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
