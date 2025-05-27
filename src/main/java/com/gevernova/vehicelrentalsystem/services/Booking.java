package com.gevernova.vehicelrentalsystem.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
public class Booking {
    private String bookingId;
    private String vehicleId;
    private String customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private int customerRating; // Default to 0 or -1 if not rated
    private double distanceTraveled; // Added as per problem statement

    /**
     * Constructor for Booking.
     */
    public Booking(String bookingId, String vehicleId, String customerId, LocalDateTime startTime, LocalDateTime endTime) {
        if (bookingId == null || bookingId.trim().isEmpty() ||
                vehicleId == null || vehicleId.trim().isEmpty() ||
                customerId == null || customerId.trim().isEmpty() ||
                startTime == null || endTime == null || startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Invalid booking parameters. All fields must be non-null/non-empty, and start time must be before end time.");
        }
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerRating = 0; // Default: 0 indicates no rating yet
        this.distanceTraveled = 0.0; // Default: no distance recorded yet
    }

    // Getters for booking properties
    public String getBookingId() { return bookingId; }
    public String getVehicleId() { return vehicleId; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getTotalPrice() { return totalPrice; }
    public int getCustomerRating() { return customerRating; }
    public double getDistanceTraveled() { return distanceTraveled; }

    // Setters for mutable properties (total price, rating, distance traveled)
    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative.");
        }
        this.totalPrice = totalPrice;
    }
    public void setCustomerRating(int customerRating) {
        // Validation for rating range is typically done in the service layer, but a basic check here can be useful.
        if (customerRating < 1 || customerRating > 5) {
            throw new IllegalArgumentException("Customer rating must be between 1 and 5.");
        }
        this.customerRating = customerRating;
    }
    public void setDistanceTraveled(double distanceTraveled) {
        if (distanceTraveled < 0) {
            throw new IllegalArgumentException("Distance traveled cannot be negative.");
        }
        this.distanceTraveled = distanceTraveled;
    }

    /**
     * Calculates the duration of the booking in full days.
     */
    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    /**
     * Calculates the duration of the booking in full hours.
     */
    public long getDurationInHours() {
        return ChronoUnit.HOURS.between(startTime, endTime);
    }

    /**
     * Checks if this booking's time period overlaps with another booking's time period.
     */
    public boolean overlaps(Booking other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    /**
     * Provides a string representation of the Booking object.
     */
    @Override
    public String toString() {
        return String.format("Booking{id='%s', vehicleId='%s', customerId='%s', " +
                        "startTime=%s, endTime=%s, price=%.2f, rating=%d, distance=%.2f}",
                bookingId, vehicleId, customerId, startTime, endTime, totalPrice, customerRating, distanceTraveled);
    }
}
