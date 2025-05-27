package com.gevernova.vehicelrentalsystem.exceptionhandling;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
