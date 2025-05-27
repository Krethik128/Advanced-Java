package com.gevernova.movingbookingsystem.model;

public class SeatStatus {
    public enum Status {
        AVAILABLE, HELD, BOOKED
    }

    private Seat seat;
    private Status status;
    private String heldByUserId; // Null if not held

    public SeatStatus(Seat seat, Status status) {
        this.seat = seat;
        this.status = status;
        this.heldByUserId = null;
    }

    public Seat getSeat() {
        return seat;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getHeldByUserId() {
        return heldByUserId;
    }

    public void setHeldByUserId(String heldByUserId) {
        this.heldByUserId = heldByUserId;
    }

    @Override
    public String toString() {
        return "SeatStatus{" +
                "seatId=" + seat.getId() +
                ", status=" + status +
                ", heldByUserId='" + (heldByUserId != null ? heldByUserId : "N/A") + '\'' +
                '}';
    }
}