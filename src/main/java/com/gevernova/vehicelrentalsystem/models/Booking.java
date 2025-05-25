package com.gevernova.vehicelrentalsystem.models;

/// Booking.java
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Booking {
    private String bookingId;
    private String vehicleId;
    private String customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int customerRating;
    private double totalPrice;

    public Booking(String bookingId, String vehicleId, String customerId,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getDurationInDays() {
        long days = ChronoUnit.DAYS.between(startTime, endTime);
        return days == 0 ? 1 : days;
    }

    public long getDurationInHours() {
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        return hours == 0 ? 1 : hours;
    }

    public boolean overlaps(Booking other) {
        return startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime);
    }

    // Getters and setters
    public String getBookingId() { return bookingId; }
    public String getVehicleId() { return vehicleId; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getCustomerRating() { return customerRating; }
    public double getTotalPrice() { return totalPrice; }

    public void setCustomerRating(int customerRating) { this.customerRating = customerRating; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return String.format("Booking{id='%s', vehicleId='%s', price=%.2f}",
                bookingId, vehicleId, totalPrice);
    }
}
