package com.gevernova.movingbookingsystem.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Show {
    private String id;
    private Movie movie; // Association with Movie
    private Multiplex multiplex; // Association with Multiplex
    private Screen screen; // Association with Screen
    private Date showTime;
    // Map to store seat status for this specific show (Seat ID -> SeatStatus)
    private Map<String, SeatStatus> seatStatuses;

    public Show(String id, Movie movie, Multiplex multiplex, Screen screen, Date showTime) {
        this.id = id;
        this.movie = movie;
        this.multiplex = multiplex;
        this.screen = screen;
        this.showTime = showTime;
        this.seatStatuses = new HashMap<>();
        // Initialize all seats in the screen as AVAILABLE for this show
        for (Seat seat : screen.getSeats()) {
            seatStatuses.put(seat.getId(), new SeatStatus(seat, SeatStatus.Status.AVAILABLE));
        }
    }

    public String getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Multiplex getMultiplex() {
        return multiplex;
    }

    public Screen getScreen() {
        return screen;
    }

    public Date getShowTime() {
        return showTime;
    }

    public Map<String, SeatStatus> getSeatStatuses() {
        return seatStatuses;
    }

    public SeatStatus getSeatStatus(String seatId) {
        return seatStatuses.get(seatId);
    }

    public void updateSeatStatus(String seatId, SeatStatus.Status newStatus, String userId) {
        SeatStatus currentSeatStatus = seatStatuses.get(seatId);
        if (currentSeatStatus != null) {
            currentSeatStatus.setStatus(newStatus);
            if (newStatus == SeatStatus.Status.HELD) {
                currentSeatStatus.setHeldByUserId(userId);
            } else {
                currentSeatStatus.setHeldByUserId(null); // Clear held status if not held
            }
        }
    }

    @Override
    public String toString() {
        return "Show{" +
                "id='" + id + '\'' +
                ", movie=" + movie.getTitle() +
                ", multiplex=" + multiplex.getName() +
                ", screen=" + screen.getName() +
                ", showTime=" + showTime +
                '}';
    }
}