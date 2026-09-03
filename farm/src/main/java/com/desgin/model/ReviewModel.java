package com.desgin.model;

import java.util.ArrayList;
import java.util.List;

public class ReviewModel {

    private String reviewId;
    private String bookingId;
    private String machineryName;
    private String farmerEmail;
    private String farmerName;
    private int rating;
    private String headline;
    private String comment;
    private String date;
    private List<String> tags;
    private String providerReply;
    private boolean isOperator;
    private long timestamp;

    public ReviewModel() {
        this.tags = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
    }

    public ReviewModel(String reviewId, String bookingId, String machineryName, String farmerEmail,
                       String farmerName, int rating, String headline, String comment,
                       String date, List<String> tags, String providerReply, boolean isOperator) {
        this.reviewId = reviewId;
        this.bookingId = bookingId;
        this.machineryName = machineryName;
        this.farmerEmail = farmerEmail;
        this.farmerName = farmerName;
        this.rating = rating;
        this.headline = headline;
        this.comment = comment;
        this.date = date;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.providerReply = providerReply;
        this.isOperator = isOperator;
        this.timestamp = System.currentTimeMillis();
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getMachineryName() { return machineryName; }
    public void setMachineryName(String machineryName) { this.machineryName = machineryName; }

    public String getFarmerEmail() { return farmerEmail; }
    public void setFarmerEmail(String farmerEmail) { this.farmerEmail = farmerEmail; }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getProviderReply() { return providerReply; }
    public void setProviderReply(String providerReply) { this.providerReply = providerReply; }

    public boolean isOperator() { return isOperator; }
    public void setOperator(boolean operator) { isOperator = operator; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}