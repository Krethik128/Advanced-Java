package com.gevernova.onlinebooksystem.services;

public class FlatDiscount implements DiscountStrategy {
    private double flatAmount;

    public FlatDiscount(double flatAmount) {
        this.flatAmount = flatAmount;
    }

    public double applyDiscount(double totalAmount) {
        return Math.max(0, totalAmount - flatAmount);
    }
}
