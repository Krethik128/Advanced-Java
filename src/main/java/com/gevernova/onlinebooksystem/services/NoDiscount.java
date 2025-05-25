package com.gevernova.onlinebooksystem.services;

public class NoDiscount implements DiscountStrategy {
    public double applyDiscount(double totalAmount) {
        return totalAmount;
    }
}
