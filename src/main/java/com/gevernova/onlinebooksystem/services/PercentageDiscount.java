package com.gevernova.onlinebooksystem.services;

public class PercentageDiscount implements DiscountStrategy {
    private double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    public double applyDiscount(double totalAmount) {
        return totalAmount * (1 - percentage / 100);
    }
}
