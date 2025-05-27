package com.gevernova.onlinebooksystem.models;

public class FlatDiscount implements DiscountStrategy {
    private double flatAmount;

    public FlatDiscount(double flatAmount) {
        if (flatAmount < 0) {
            throw new IllegalArgumentException("Flat discount amount cannot be negative.");
        }
        this.flatAmount = flatAmount;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return Math.max(0, totalAmount - flatAmount);
    }
}
