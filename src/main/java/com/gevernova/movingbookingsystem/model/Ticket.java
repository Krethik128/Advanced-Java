package com.gevernova.movingbookingsystem.model;

import static com.gevernova.IDGenerator.generateID;

public class Ticket {
    Show show;
    private final String ticketId;
    private final String showId;
    private final String seatId;


    public Ticket(String seatId,String showId) {
        show =new Show();
        this.ticketId = generateID();
        this.showId = show.getShowId();
        this.seatId = seatId;
    }
    public String getTicketId() {
        return ticketId;
    }
    public String getShowId() {
        return showId;
    }
    public String getSeatId() {
        return seatId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketId.equals(ticket.ticketId);
    }
    @Override
    public int hashCode() {
        return ticketId.hashCode();
    }
}
