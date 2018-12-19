package com.trendyol.model;

public class Product {

    private String date;
    private String productId;
    private String eventName;
    private String userId;

    public Product(String date, String productId, String eventName, String userId) {
        this.date = date;
        this.productId = productId;
        this.eventName = eventName;
        this.userId = userId;
    }

    public Product() {

    }

    public String getDate() {
        return date;
    }

    public String getProductId() {
        return productId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getUserId() {
        return userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}