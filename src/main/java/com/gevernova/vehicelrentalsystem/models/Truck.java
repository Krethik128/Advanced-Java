package com.gevernova.vehicelrentalsystem.models;
// Truck.java
public class Truck extends Vehicle {
    public Truck(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }

    @Override
    public String getVehicleType() {
        return "Truck";
    }
}
