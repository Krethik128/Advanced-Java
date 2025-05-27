package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BookingService {
    private ShowSchedulingService showSchedulingService; // Dependency
    private UserService userService; // Dependency
    private TicketService ticketService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long HOLD_DURATION_SECONDS = 300; // 5 minutes

    public BookingService(ShowSchedulingService showSchedulingService, UserService userService) {
        this.showSchedulingService = showSchedulingService;
        this.userService = userService;
    }

    // Get available seats for a show
    public List<SeatStatus> getAvailableSeats(String showId) {
        Show show = showSchedulingService.getShowById(showId);
        if (show == null) {
            throw new IllegalArgumentException("Show with ID " + showId + " not found.");
        }
        return show.getSeatStatuses().values().stream()
                .filter(ss -> ss.getStatus() == SeatStatus.Status.AVAILABLE)
                .collect(Collectors.toList());
    }

    // Hold seats for a user (temporary reservation)
    public List<SeatStatus> holdSeats(String showId, List<String> seatIds, String userId) {
        Show show = showSchedulingService.getShowById(showId);
        User user = userService.getUserProfile(userId);

        if (show == null) {
            throw new IllegalArgumentException("Show with ID " + showId + " not found.");
        }
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        List<SeatStatus> heldStatuses = new ArrayList<>();
        for (String seatId : seatIds) {
            SeatStatus seatStatus = show.getSeatStatus(seatId);
            if (seatStatus == null) {
                throw new IllegalArgumentException("Seat " + seatId + " not found in show " + showId + ".");
            }
            if (seatStatus.getStatus() != SeatStatus.Status.AVAILABLE) {
                throw new IllegalStateException("Seat " + seatId + " is not available. Current status: " + seatStatus.getStatus());
            }
            // Mark as HELD by user
            show.updateSeatStatus(seatId, SeatStatus.Status.HELD, userId);
            heldStatuses.add(seatStatus);
        }

        System.out.println("Seats " + seatIds + " held for " + user.getUsername() + " for show " + show.getId());

        // Schedule a task to release held seats if not booked within HOLD_DURATION_SECONDS
        scheduler.schedule(() -> {
            for (String seatId : seatIds) {
                SeatStatus seatStatus = show.getSeatStatus(seatId);
                if (seatStatus != null && seatStatus.getStatus() == SeatStatus.Status.HELD && userId.equals(seatStatus.getHeldByUserId())) {
                    show.updateSeatStatus(seatId, SeatStatus.Status.AVAILABLE, null);
                    System.out.println("Held seat " + seatId + " released for show " + show.getId() + " due to timeout.");
                }
            }
        }, HOLD_DURATION_SECONDS, TimeUnit.SECONDS);

        return heldStatuses;
    }

    // Book seats (finalize reservation)
    public List<Ticket> bookSeats(String showId, List<String> seatIds, String userName, PaymentDetails paymentDetails) {
        Show show = showSchedulingService.getShowById(showId);
        User user = userService.getUserProfile(userName);

        if (show == null) {
            throw new IllegalArgumentException("Show with ID " + showId + " not found.");
        }
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userName + " not found.");
        }
        if (paymentDetails == null || paymentDetails.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid payment details.");
        }

        List<Ticket> newTickets = new ArrayList<>();
        double totalPrice = 0; // Assuming a fixed price per ticket for simplicity, or dynamic pricing
        double ticketPrice = 120.0; // Example price

        for (String seatId : seatIds) {
            SeatStatus seatStatus = show.getSeatStatus(seatId);
            if (seatStatus == null) {
                throw new IllegalArgumentException("Seat " + seatId + " not found in show " + showId + ".");
            }
            // Seats must be either AVAILABLE or HELD by the current user
            if (seatStatus.getStatus() == SeatStatus.Status.BOOKED) {
                throw new IllegalStateException("Seat " + seatId + " is already booked.");
            }
            if (seatStatus.getStatus() == SeatStatus.Status.HELD && !userName.equals(seatStatus.getHeldByUserId())) {
                throw new IllegalStateException("Seat " + seatId + " is held by another user.");
            }
            // Perform payment simulation here (in a real system, this integrates with a payment gateway)
            // For simplicity, we just check if the provided payment amount covers the cost
            totalPrice += ticketPrice;
        }


        // Process payment () SINGLE RESPONSIBILITY PAYMENT SERVICES taking care or payment
        PaymentService paymentService = new PaymentService();
        boolean paymentSuccessful=paymentService.processPayment(paymentDetails, paymentDetails.getAmount(),totalPrice);

        if (paymentSuccessful) {
            for (String seatId : seatIds) {
                Seat seat = show.getScreen().getSeats().stream()
                        .filter(s -> s.getId().equals(seatId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Seat object not found for ID: " + seatId));

                show.updateSeatStatus(seatId, SeatStatus.Status.BOOKED, null); // Mark as BOOKED, clear held status
                String ticketId = IDGenerator.generateUniqueId("TKT");
                Ticket ticket = new Ticket(ticketId, user, show, seat, ticketPrice);
                ticketService.addTicket(ticket);
                newTickets.add(ticket);
            }
            System.out.println("Booking successful for user " + user.getUsername() + ". Tickets: " + newTickets.stream().map(Ticket::getId).collect(Collectors.joining(", ")));
            return newTickets;
        } else {
            throw new IllegalStateException("Payment failed for booking.");
        }
    }

    // Cancel a ticket
    public boolean cancelBooking(String ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket with ID " + ticketId + " not found.");
            return false;
        }

        Show show = ticket.getShow();
        Seat seat = ticket.getSeat();

        // Update seat status back to AVAILABLE
        show.updateSeatStatus(seat.getId(), SeatStatus.Status.AVAILABLE, null);
        ticketService.removeTicket(ticketId);
        System.out.println("Ticket " + ticketId + " cancelled. Seat " + seat.getId() + " is now available for show " + show.getId());
        return true;
    }
}