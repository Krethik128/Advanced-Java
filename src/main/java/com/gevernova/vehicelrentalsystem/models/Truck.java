package com.gevernova.vehicelrentalsystem.models;
// Truck.java
public class Truck extends Vehicle {
    private final double cargoCapacityTons;
    public Truck(String vehicleId, String brand, String model, double basePricePerDay, double cargoCapacityTons) {
        super(vehicleId, brand, model, basePricePerDay);
        if (cargoCapacityTons <= 0) {
            throw new IllegalArgumentException("Cargo capacity must be positive for a Truck.");
        }
        this.cargoCapacityTons = cargoCapacityTons;
    }

    @Override
    public String getVehicleType() {
        return "Truck";
    }

    public double getCargoCapacityTons() {
        return cargoCapacityTons;
    }

    @Override
    public String toString() {
        return String.format("%s, cargoCapacity=%.2f tons}", super.toString().substring(0, super.toString().length() - 1), cargoCapacityTons);
    }

}
