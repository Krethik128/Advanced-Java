package com.gevernova.vehicelrentalsystem.models;

public class SUV extends Vehicle{
    private final boolean fourWheelDrive;
    public SUV(String vehicleId, String brand, String model, double basePricePerDay, boolean fourWheelDrive) {
        super(vehicleId, brand, model, basePricePerDay);
        this.fourWheelDrive = fourWheelDrive;
    }
    @Override
    public String getVehicleType() {
        return "SUV";
    }

    public boolean isFourWheelDrive() {
        return fourWheelDrive;
    }
    @Override
    public String toString() {
        return String.format("%s, 4WD=%b}", super.toString().substring(0, super.toString().length() - 1), fourWheelDrive);
    }

}
