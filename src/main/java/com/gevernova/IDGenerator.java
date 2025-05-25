package com.gevernova;

public class IDGenerator {
    public static String generateID() {
        return java.util.UUID.randomUUID().toString();
    }
}
