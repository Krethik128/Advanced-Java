package com.gevernova.vehicelrentalsystem.services;

import com.gevernova.vehicelrentalsystem.models.Booking;
import com.gevernova.vehicelrentalsystem.models.Vehicle;

// PerDayPricingStrategy.java
public class PerDayPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Vehicle vehicle, Booking booking) {
        return vehicle.getBasePricePerDay() * booking.getDurationInDays();
    }
}
