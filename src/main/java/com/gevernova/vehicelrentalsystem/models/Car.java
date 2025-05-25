package com.gevernova.vehicelrentalsystem.models;

// Car.java
public class Car extends Vehicle {
    public Car(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}
