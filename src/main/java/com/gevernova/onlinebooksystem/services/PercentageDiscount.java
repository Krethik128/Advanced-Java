package com.gevernova.onlinebooksystem.services;

import com.gevernova.onlinebooksystem.models.DiscountStrategy;

public class PercentageDiscount implements DiscountStrategy {
    private double percentage; // Stored as 0-100, e.g., 10 for 10%

    public PercentageDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount * (1 - percentage / 100.0);
    }
}
