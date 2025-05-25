package com.gevernova.movingbookingsystem.model;

public class Seat {
    private String seatNumber;
    private final Category category;
    private SeatStatus seatStatus;

    public Seat(Category category, String seatNumber)  {
        this.category = category;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus.AVAILABLE;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) { // Setter allows updating seat status
        this.seatStatus = seatStatus;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return seatNumber.equals(seat.seatNumber);
    }

    @Override
    public int hashCode() {
        return seatNumber.hashCode();
    }

}
