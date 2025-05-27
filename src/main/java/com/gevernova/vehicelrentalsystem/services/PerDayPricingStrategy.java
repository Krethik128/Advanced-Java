package com.gevernova.vehicelrentalsystem.services;

import com.gevernova.vehicelrentalsystem.models.Vehicle;

// PerDayPricingStrategy.java
public class PerDayPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Vehicle vehicle, Booking booking) {
        if (vehicle == null || booking == null) {
            throw new IllegalArgumentException("Vehicle and Booking cannot be null for price calculation.");
        }
        long durationDays = booking.getDurationInDays();
        if (durationDays == 0 && booking.getDurationInHours() > 0) {
            durationDays = 1; // If duration is less than a day but not zero, charge for one day.
        }
        return vehicle.getBasePricePerDay() * durationDays;
    }
}
