package com.gevernova.vehicelrentalsystem.services;

import com.gevernova.vehicelrentalsystem.models.Booking;
import com.gevernova.vehicelrentalsystem.models.Vehicle;

// PerHourPricingStrategy.java
public class PerHourPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Vehicle vehicle, Booking booking) {
        double pricePerHour = vehicle.getBasePricePerDay() / 24.0;
        return pricePerHour * booking.getDurationInHours();
    }
}
