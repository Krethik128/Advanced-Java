package com.gevernova.onlinebooksystem.services;

import com.gevernova.onlinebooksystem.models.DiscountStrategy;

public class DiscountCalculator {

    public double calculateFinalAmount(double totalAmount, DiscountStrategy discountStrategy) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative.");
        }
        if (discountStrategy == null) {
            // Default to no discount if strategy is null (or throw an exception based on business rules)
            return totalAmount;
        }
        return discountStrategy.applyDiscount(totalAmount);
    }
}
