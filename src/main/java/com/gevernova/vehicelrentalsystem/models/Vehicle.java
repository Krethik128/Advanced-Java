package com.gevernova.vehicelrentalsystem.models;
// Vehicle.java
public abstract class Vehicle {
    private String vehicleId;
    private String brand;
    private String model;
    private double basePricePerDay;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
    }

    public abstract String getVehicleType();

    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getBasePricePerDay() { return basePricePerDay; }

    @Override
    public String toString() {
        return String.format("%s{id='%s', brand='%s', model='%s'}",
                getVehicleType(), vehicleId, brand, model);
    }
}
