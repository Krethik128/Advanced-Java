package com.gevernova.feedbackaggregator.model;
public class Feedback {
    private final String customerName;
    private final int rating; // 1 to 5
    private final String comment;

    public Feedback(String customerName, int rating, String comment) {
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}

