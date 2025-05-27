package com.gevernova.movingbookingsystem.model;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {
    private static final AtomicLong counter = new AtomicLong();

    public static String generateUniqueId(String prefix) {
        return prefix + counter.incrementAndGet();
    }
}
