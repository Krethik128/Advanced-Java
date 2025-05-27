package com.gevernova.vehicelrentalsystem.models;

// Car.java
public class Car extends Vehicle {
    private final int numberOfSeats;
    public Car(String vehicleId, String brand, String model, double basePricePerDay, int numberOfSeats) {
        super(vehicleId, brand, model, basePricePerDay);
        if(vehicleId == null || vehicleId.trim().isEmpty()){
            throw new IllegalArgumentException("Invalid vehicle parameters during creation.");
        }
        if (numberOfSeats <= 0) {
            throw new IllegalArgumentException("Number of seats must be positive for a Car.");
        }
        this.numberOfSeats = numberOfSeats;
    }
    // Getters
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }


    @Override
    public String toString() {
        return String.format("%s, seats=%d}", super.toString().substring(0, super.toString().length() - 1), numberOfSeats);
    }
}
