package com.gevernova.vehicelrentalsystem.services;

import com.gevernova.vehicelrentalsystem.models.Booking;
import com.gevernova.vehicelrentalsystem.models.Vehicle;

// PricingStrategy.java
public interface PricingStrategy {
    double calculatePrice(Vehicle vehicle, Booking booking);
}


