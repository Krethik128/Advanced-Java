package com.gevernova.movingbookingsystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gevernova.IDGenerator.generateID;

public class Show {
    public List<Seat> seats;
    private Movie movie;
    private String showId;
    private int showDuration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isShowActive;
    private boolean isShowFull;
    private int seatsAvailable;
    private final int  MAX_SEATS_AVAILABLE=100;


    public Show() {
        this.showId = generateID();
        this.seats = new ArrayList<>(MAX_SEATS_AVAILABLE);
        this.isShowActive = true; // A new show is active by default
        this.isShowFull = false;  // A new show is not full by default
        this.seatsAvailable = 0; // Seats will be added and counted by BookingServices
    }

    public Show(Movie movie, LocalDateTime startTime, LocalDateTime endTime) {
        this(); // Call the default constructor for common initializations
        this.movie = movie;
        this.showDuration = movie.getMovieDuration(); // Use duration from the Movie object
        this.startTime = startTime;
        this.endTime = endTime;
        // isShowActive, isShowFull, seats, showId, and seatsAvailable are initialized by the default constructor
    }

    public void setShowFull(boolean showFull) {
        isShowFull = showFull;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public void setShowActive(boolean showActive) {
        isShowActive = showActive;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getShowDuration() {
        return showDuration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isShowFull() {
        return isShowFull;
    }

    public boolean isShowActive() {
        return isShowActive;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public int getMAX_SEATS_AVAILABLE() {
        return MAX_SEATS_AVAILABLE;
    }
    public String getShowId() {
        return showId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return showId.equals(show.showId);
    }
    @Override
    public int hashCode() {
        return showId.hashCode();
    }
}
