package com.gevernova.vehicelrentalsystem.services;

import com.gevernova.vehicelrentalsystem.models.Vehicle;

// PerHourPricingStrategy.java
public class PerHourPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Vehicle vehicle, Booking booking) {
        if (vehicle == null || booking == null) {
            throw new IllegalArgumentException("Vehicle and Booking cannot be null for price calculation.");
        }
        double pricePerHour = vehicle.getBasePricePerDay() / 24.0; // Convert daily rate to hourly
        long durationHours = booking.getDurationInHours();
        // Ensure at least one hour is charged if duration is positive but less than one hour.
        if (durationHours == 0 && (booking.getEndTime().getMinute() != booking.getStartTime().getMinute() ||
                booking.getEndTime().getSecond() != booking.getStartTime().getSecond())) {
            durationHours = 1; // If duration is less than an hour but not zero, charge for one hour.
        }
        return pricePerHour * durationHours;
    }
}
