package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketService {
    private Map<String, Ticket> tickets; // Key: ticketId, Value: Ticket object

    public TicketService() {
        this.tickets = new HashMap<>();
    }
    // Get tickets for a user
    public void addTicket(Ticket ticket) {
        if (ticket == null || ticket.getId() == null) {
            throw new IllegalArgumentException("Ticket and Ticket ID cannot be null.");
        }
        if (tickets.containsKey(ticket.getId())) {
            System.err.println("Warning: Ticket with ID " + ticket.getId() + " already exists. Overwriting.");
            // Or throw new IllegalArgumentException("Ticket with ID " + ticket.getTicketId() + " already exists.");
        }
        this.tickets.put(ticket.getId(), ticket);
        System.out.println("TicketService: Ticket " + ticket.getId() + " added.");
    }
    public List<Ticket> getTicketsForUser(String userId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Ticket> getAllTickets() {
        return new java.util.ArrayList<>(tickets.values());
    }
    public Ticket removeTicket(String ticketId) {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket ID cannot be null for removal.");
        }
        System.out.println("TicketService: Ticket " + ticketId + " removed.");
        return this.tickets.remove(ticketId);
    }

    public Ticket getTicketById(String ticketId){
        return tickets.get(ticketId);
    }
}
