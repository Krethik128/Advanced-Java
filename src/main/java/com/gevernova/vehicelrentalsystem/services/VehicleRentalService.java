package com.gevernova.vehicelrentalsystem.services;

// VehicleRentalService.java
import com.gevernova.vehicelrentalsystem.exceptionhandling.BookingOverlapException;
import com.gevernova.vehicelrentalsystem.exceptionhandling.InvalidRatingException;
import com.gevernova.vehicelrentalsystem.models.Vehicle;

import java.time.LocalDateTime;
import java.util.*;

public class VehicleRentalService {
    // Manages the inventory of vehicles. This is VehicleRentalService's core responsibility.
    private Map<String, Vehicle> vehicles;
    // Delegates all booking-related operations to BookingService, adhering to SRP.
    private BookingService bookingService;

    public VehicleRentalService(PricingStrategy pricingStrategy) {
        this.vehicles = new HashMap<>();
        // Inject the pricing strategy into the BookingService, making it flexible (OCP for pricing)
        this.bookingService = new BookingService(pricingStrategy);
    }

    public void addVehicle(Vehicle vehicle) throws IllegalArgumentException {
        if(vehicle.getVehicleId()==null){
            throw new IllegalArgumentException("Vehicle cannot be null for vehicle creation.");
        }
        if (vehicle.getVehicleId() == null || vehicle.getVehicleId().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or empty.");
        }
        if (vehicles.containsKey(vehicle.getVehicleId())) {
            throw new IllegalArgumentException("Vehicle with ID " + vehicle.getVehicleId() + " already exists.");
        }
        vehicles.put(vehicle.getVehicleId(), vehicle);
        System.out.println("Added vehicle: " + vehicle.getVehicleType() + " " + vehicle.getBrand() + " " + vehicle.getModel() + " (ID: " + vehicle.getVehicleId() + ")");
    }

    public Vehicle getVehicle(String vehicleId) {
        return vehicles.get(vehicleId);
    }

    public Booking createBooking(String bookingId, String vehicleId, String customerId,
                                 LocalDateTime startTime, LocalDateTime endTime) throws BookingOverlapException, IllegalArgumentException {
        Vehicle vehicle = getVehicle(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found with ID: " + vehicleId);
        }
        // Delegate the complex booking creation and overlap checking logic to BookingService (SRP)
        return bookingService.createBooking(bookingId, vehicleId, customerId, startTime, endTime, vehicle);
    }

    public void completeBooking(String bookingId, int rating, double distanceTraveled) throws InvalidRatingException, IllegalArgumentException {
        // Delegate booking completion to BookingService (SRP)
        bookingService.completeBooking(bookingId, rating, distanceTraveled);
    }

    public List<Booking> getBookingsForVehicle(String vehicleId) {
        return bookingService.getBookingsForVehicle(vehicleId);
    }

    public double getAverageRating(String vehicleId) {
        return bookingService.getAverageRating(vehicleId);
    }
    /**
     * Retrieves a collection of all vehicles currently in the rental system.
     * @return A collection of Vehicle objects.
     */
    public Collection<Vehicle> getAllVehicles() {
        return vehicles.values();
    }
}

