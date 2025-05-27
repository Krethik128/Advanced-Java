package com.gevernova.movingbookingsystem.model;

import java.util.Date;

public class Ticket {
    private String id;
    private User user; // Association with User
    private Show show; // Association with Show
    private Seat seat; // Association with Seat
    private Date bookingTime;
    private double price;

    public Ticket(String id, User user, Show show, Seat seat, double price) {
        this.id = id;
        this.user = user;
        this.show = show;
        this.seat = seat;
        this.bookingTime = new Date(); // Current time
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public Seat getSeat() {
        return seat;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", user=" + user.getUsername() +
                ", movie=" + show.getMovie().getTitle() +
                ", showTime=" + show.getShowTime() +
                ", seat=" + seat.getId() +
                ", price=" + price +
                '}';
    }
}