package com.gevernova.movingbookingsystem.model;

public class Seat {
    private String id; // Unique seat identifier (e.g., A1, B5)
    private String rowIdentifier;
    private int columnNumber;

    public Seat(String id, String rowIdentifier, int columnNumber) {
        this.id = id;
        this.rowIdentifier = rowIdentifier;
        this.columnNumber = columnNumber;
    }

    public String getId() {
        return id;
    }

    public String getRowIdentifier() {
        return rowIdentifier;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id='" + id + '\'' +
                '}';
    }
}