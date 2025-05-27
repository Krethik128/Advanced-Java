package com.gevernova.movingbookingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private String id;
    private String name;
    private int capacity;
    private List<Seat> seats; // Seats belonging to this screen

    public Screen(String id, String name, int rows, int cols) {
        this.id = id;
        this.name = name;
        this.seats = new ArrayList<>();
        this.capacity = rows * cols;
        initializeSeats(rows, cols);
    }

    private void initializeSeats(int rows, int cols) {
        for (char rowChar = 'A'; rowChar < ('A' + rows); rowChar++) {
            for (int colNum = 1; colNum <= cols; colNum++) {
                seats.add(new Seat(rowChar + String.valueOf(colNum), String.valueOf(rowChar), colNum));
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}