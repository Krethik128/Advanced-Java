package com.gevernova.vehicelrentalsystem.services;

// VehicleRentalService.java
import com.gevernova.vehicelrentalsystem.models.Booking;
import com.gevernova.vehicelrentalsystem.models.Vehicle;

import java.time.LocalDateTime;
import java.util.*;

public class VehicleRentalService {
    private Map<String, Vehicle> vehicles = new HashMap<>();
    private Map<String, List<Booking>> bookingsByVehicle = new HashMap<>();
    private PricingStrategy pricingStrategy = new PerDayPricingStrategy();

    public void addVehicle(Vehicle vehicle) {
        vehicles.put(vehicle.getVehicleId(), vehicle);
    }

    public Vehicle getVehicle(String vehicleId) {
        return vehicles.get(vehicleId);
    }

    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    public Booking createBooking(String bookingId, String vehicleId, String customerId,
                                 LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        Vehicle vehicle = getVehicle(vehicleId);
        if (vehicle == null) {
            throw new Exception("Vehicle not found: " + vehicleId);
        }

        Booking newBooking = new Booking(bookingId, vehicleId, customerId, startTime, endTime);

        // Check for overlapping bookings
        List<Booking> existingBookings = bookingsByVehicle.getOrDefault(vehicleId, new ArrayList<>());
        for (Booking existing : existingBookings) {
            if (newBooking.overlaps(existing)) {
                throw new Exception("Booking overlap detected for vehicle: " + vehicleId);
            }
        }

        // Calculate price
        double price = pricingStrategy.calculatePrice(vehicle, newBooking);
        newBooking.setTotalPrice(price);

        // Add booking
        existingBookings.add(newBooking);
        bookingsByVehicle.put(vehicleId, existingBookings);

        return newBooking;
    }

    public void completeBooking(String bookingId, int rating) throws Exception {
        if (rating < 1 || rating > 5) {
            throw new Exception("Invalid rating: " + rating + ". Must be 1-5.");
        }

        for (List<Booking> bookings : bookingsByVehicle.values()) {
            for (Booking booking : bookings) {
                if (booking.getBookingId().equals(bookingId)) {
                    booking.setCustomerRating(rating);
                    return;
                }
            }
        }
        throw new Exception("Booking not found: " + bookingId);
    }

    public List<Booking> getBookingsForVehicle(String vehicleId) {
        return bookingsByVehicle.getOrDefault(vehicleId, new ArrayList<>());
    }

    public double getAverageRating(String vehicleId) {
        List<Booking> bookings = getBookingsForVehicle(vehicleId);
        return bookings.stream()
                .filter(b -> b.getCustomerRating() > 0)
                .mapToInt(Booking::getCustomerRating)
                .average()
                .orElse(0.0);
    }
}

