package com.gevernova.vehicelrentalsystem.services;

import com.gevernova.vehicelrentalsystem.exceptionhandling.BookingOverlapException;
import com.gevernova.vehicelrentalsystem.exceptionhandling.InvalidRatingException;
import com.gevernova.vehicelrentalsystem.models.Vehicle;

import java.time.LocalDateTime;
import java.util.*;

public class BookingService {
    // Stores bookings organized by vehicle ID. This map is the responsibility of BookingService.
    private final Map<String, List<Booking>> bookingsByVehicle;
    private final PricingStrategy pricingStrategy;

    public BookingService(PricingStrategy pricingStrategy) {
        this.bookingsByVehicle = new HashMap<>();
        this.pricingStrategy = pricingStrategy;
    }

    public Booking createBooking(String bookingId, String vehicleId, String customerId,
                                 LocalDateTime startTime, LocalDateTime endTime, Vehicle vehicle) throws BookingOverlapException, IllegalArgumentException {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null for booking creation.");
        }

        // Create the new booking instance
        Booking newBooking = new Booking(bookingId, vehicleId, customerId, startTime, endTime);

        // Retrieve existing bookings for this vehicle or create a new list
        List<Booking> existingBookings = bookingsByVehicle.computeIfAbsent(vehicleId, k -> new ArrayList<>());

        // Check for overlapping bookings to prevent double-booking
        for (Booking existing : existingBookings) {
            if (newBooking.overlaps(existing)) {
                throw new BookingOverlapException("Booking overlap detected for vehicle: " + vehicleId +
                        " (Booking " + existing.getBookingId() + " overlaps with requested booking " + bookingId + ")");
            }
        }

        // Calculate price using the injected pricing strategy (OCP)
        double price = pricingStrategy.calculatePrice(vehicle, newBooking);
        newBooking.setTotalPrice(price);

        // Add the new booking to the list for the vehicle
        existingBookings.add(newBooking);

        System.out.println("Booking created: " + newBooking.getBookingId() + " for vehicle " + newBooking.getVehicleId() + " (Price: " + String.format("%.2f", newBooking.getTotalPrice()) + ")");
        return newBooking;
    }

    public void completeBooking(String bookingId, int rating, double distanceTraveled) throws InvalidRatingException, IllegalArgumentException {
        // Validate rating first using custom exception
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException("Invalid rating: " + rating + ". Rating must be between 1 and 5.");
        }
        if (distanceTraveled < 0) {
            throw new IllegalArgumentException("Distance traveled cannot be negative.");
        }

        Booking foundBooking = findBookingById(bookingId);
        if (foundBooking != null) {
            foundBooking.setCustomerRating(rating);
            foundBooking.setDistanceTraveled(distanceTraveled);
            System.out.println("Booking " + bookingId + " completed with rating " + rating + " and distance " + String.format("%.2f", distanceTraveled) + " km.");
        } else {
            throw new IllegalArgumentException("Booking not found with ID: " + bookingId);
        }
    }

    private Booking findBookingById(String bookingId) {
        // Iterate through all lists of bookings to find the matching one
        for (List<Booking> bookings : bookingsByVehicle.values()) {
            for (Booking booking : bookings) {
                if (booking.getBookingId().equals(bookingId)) {
                    return booking;
                }
            }
        }
        return null;
    }

    public List<Booking> getBookingsForVehicle(String vehicleId) {
        return new ArrayList<>(bookingsByVehicle.getOrDefault(vehicleId, Collections.emptyList()));
    }

    public double getAverageRating(String vehicleId) {
        List<Booking> bookings = getBookingsForVehicle(vehicleId);
        return bookings.stream()
                .filter(b -> b.getCustomerRating() > 0) // Only consider bookings that have been rated
                .mapToInt(Booking::getCustomerRating)
                .average()
                .orElse(0.0); // Return 0.0 if no ratings are available
    }


}
