package com.gevernova.movingbookingsystem;

import com.gevernova.movingbookingsystem.exceptions.*;
import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.services.BookingServices;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        BookingServices movieBookingService = new BookingServices();

        System.out.println("--- Starting Movie Booking System Test Cases ---");

        // 1. Initial Setup: Create Movies, Multiplexes, Users
        System.out.println("\n--- Initial Setup ---");

        Movie actionMovie = new Movie("The Martian", 141);
        Movie sciFiMovie = new Movie("Dune: Part Two", 166);
        Movie thrillerMovie = new Movie("Inception", 148);

        Multiplex grandCinema = new Multiplex("Grand Cinema");
        Multiplex cityPlex = new Multiplex("CityPlex Theatres");

        User mainBooker = new User("Alice Smith", "alice.s@example.com", "111-222-3333");
        User secondaryBooker = new User("Bob Johnson", "bob.j@example.com", "444-555-6666");
        User guestUser = new User("Charlie Brown", "charlie.b@example.com", "777-888-9999");

        // Add multiplexes and users to the booking service
        movieBookingService.addMultiplex(grandCinema);
        movieBookingService.addMultiplex(cityPlex);
        movieBookingService.addUser(mainBooker);
        movieBookingService.addUser(secondaryBooker);
        movieBookingService.addUser(guestUser);

        // Define custom prices for categories for a specific show
        Map<Category, Double> premiumPrices = new HashMap<>();
        premiumPrices.put(Category.GOLD, 250.0);
        premiumPrices.put(Category.SILVER, 180.0);
        premiumPrices.put(Category.PLATINUM, 350.0);


        // 2. Positive Test Cases

        System.out.println("\n--- Positive Test Cases ---");

        // Test Case 1: Add a show and book seats successfully
        System.out.println("\n--- Test Case 1: Successful Seat Booking ---");
        Show eveningShow = null; // Declare outside try-catch for broader scope
        List<Ticket> ticketsForFirstBooking = null; // Declare outside try-catch

        try {
            eveningShow = movieBookingService.addShowToMultiplex(
                    grandCinema.getMultiplexName(),
                    actionMovie,
                    LocalDateTime.now().plusHours(2),
                    LocalDateTime.now().plusHours(4),
                    premiumPrices
            );
            System.out.println("Available seats for show " + eveningShow.getShowId() + " initially: " +
                    movieBookingService.getAvailableSeats(grandCinema.getMultiplexName(), eveningShow.getShowId()).size());

            // Main Booker books seats GOLD-01, SILVER-05
            List<String> desiredSeatsForMainBooker = Arrays.asList("GOLD-01", "SILVER-05");
            ticketsForFirstBooking = movieBookingService.bookSeats(
                    mainBooker.getUserId(),
                    grandCinema.getMultiplexName(),
                    eveningShow.getShowId(),
                    desiredSeatsForMainBooker
            );
            System.out.println("Booked " + ticketsForFirstBooking.size() + " tickets for " + mainBooker.getName());
            System.out.println("Available seats after first booking: " +
                    movieBookingService.getAvailableSeats(grandCinema.getMultiplexName(), eveningShow.getShowId()).size());
            System.out.println("Booked seats after first booking: " +
                    movieBookingService.getBookedSeats(grandCinema.getMultiplexName(), eveningShow.getShowId()).size());

            // Verify seat status changed for GOLD-01
            Seat bookedGoldSeat = eveningShow.seats.stream().filter(s -> s.getSeatNumber().equals("GOLD-01")).findFirst().orElse(null);
            if (bookedGoldSeat != null && bookedGoldSeat.getSeatStatus() == SeatStatus.BOOKED) {
                System.out.println("Verification: GOLD-01 seat is confirmed as BOOKED.");
            } else {
                System.err.println("Verification: GOLD-01 seat status is INCORRECT (should be BOOKED).");
            }

        } catch (Exception e) {
            System.err.println("Error in Test Case 1 (Successful Booking): " + e.getMessage());
        }


        // Test Case 2: Cancel a booked ticket successfully
        System.out.println("\n--- Test Case 2: Successful Ticket Cancellation ---");
        if (eveningShow != null && ticketsForFirstBooking != null && !ticketsForFirstBooking.isEmpty()) {
            try {
                String ticketToCancelId = ticketsForFirstBooking.get(0).getTicketId(); // Take the first ticket
                boolean cancelled = movieBookingService.cancelTicket(ticketToCancelId);
                if (cancelled) {
                    System.out.println("Ticket " + ticketToCancelId + " cancelled successfully.");
                    System.out.println("Available seats after cancellation: " +
                            movieBookingService.getAvailableSeats(grandCinema.getMultiplexName(), eveningShow.getShowId()).size());
                    System.out.println("Booked seats after cancellation: " +
                            movieBookingService.getBookedSeats(grandCinema.getMultiplexName(), eveningShow.getShowId()).size());

                    // Verify seat status changed back to AVAILABLE for GOLD-01
                    Seat goldSeatAfterCancel = eveningShow.seats.stream().filter(s -> s.getSeatNumber().equals("GOLD-01")).findFirst().orElse(null);
                    if (goldSeatAfterCancel != null && goldSeatAfterCancel.getSeatStatus() == SeatStatus.AVAILABLE) {
                        System.out.println("Verification: GOLD-01 seat is confirmed as AVAILABLE after cancellation.");
                    } else {
                        System.err.println("Verification: GOLD-01 seat status is INCORRECT (should be AVAILABLE).");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in Test Case 2 (Successful Cancellation): " + e.getMessage());
            }
        } else {
            System.out.println("Skipping Test Case 2: No show or tickets available from previous test.");
        }


        // 3. Negative Test Cases

        System.out.println("\n--- Negative Test Cases ---");

        // Re-book a seat for subsequent negative tests if it was cancelled
        if (eveningShow != null) {
            try {
                movieBookingService.bookSeats(mainBooker.getUserId(), grandCinema.getMultiplexName(), eveningShow.getShowId(), Arrays.asList("GOLD-01"));
                System.out.println("GOLD-01 re-booked for subsequent negative test cases.");
            } catch (Exception e) {
                System.err.println("Could not re-book GOLD-01 for negative tests: " + e.getMessage());
            }
        }

        // Test Case 3: Try to book an already booked seat
        System.out.println("\n--- Test Case 3: Booking an already booked seat ---");
        if (eveningShow != null) {
            try {
                List<String> desiredSeatsForSecondaryBooker = Arrays.asList("GOLD-01");
                movieBookingService.bookSeats(
                        secondaryBooker.getUserId(),
                        grandCinema.getMultiplexName(),
                        eveningShow.getShowId(),
                        desiredSeatsForSecondaryBooker
                );
                System.out.println("Test Case 3 FAILED: Successfully booked an already booked seat."); // This line should not be reached
            } catch (SeatNotAvailableException e) {
                System.out.println("Test Case 3 PASSED: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error in Test Case 3 (unexpected exception): " + e.getMessage());
            }
        } else {
            System.out.println("Skipping Test Case 3: Evening show not available.");
        }


        // Test Case 4: Try to book seats for a non-existent show
        System.out.println("\n--- Test Case 4: Booking for a non-existent show ---");
        try {
            List<String> invalidSeats = Arrays.asList("PLATINUM-01", "SILVER-10");
            movieBookingService.bookSeats(
                    mainBooker.getUserId(),
                    grandCinema.getMultiplexName(),
                    "FAKE_SHOW_ID",
                    invalidSeats
            );
            System.out.println("Test Case 4 FAILED: Booked for a non-existent show."); // This line should not be reached
        } catch (ShowNotFoundException e) {
            System.out.println("Test Case 4 PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in Test Case 4 (unexpected exception): " + e.getMessage());
        }

        // Test Case 5: Try to book seats for a non-existent user
        System.out.println("\n--- Test Case 5: Booking for a non-existent user ---");
        if (eveningShow != null) {
            try {
                List<String> availableSeat = Arrays.asList("PLATINUM-02");
                movieBookingService.bookSeats(
                        "INVALID_USER_ID",
                        grandCinema.getMultiplexName(),
                        eveningShow.getShowId(),
                        availableSeat
                );
                System.out.println("Test Case 5 FAILED: Booked for a non-existent user."); // This line should not be reached
            } catch (UserNotFoundException e) {
                System.out.println("Test Case 5 PASSED: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error in Test Case 5 (unexpected exception): " + e.getMessage());
            }
        } else {
            System.out.println("Skipping Test Case 5: Evening show not available.");
        }


        // Test Case 6: Try to cancel a non-existent ticket
        System.out.println("\n--- Test Case 6: Cancelling a non-existent ticket ---");
        try {
            movieBookingService.cancelTicket("NON_EXISTENT_TICKET_ID");
            System.out.println("Test Case 6 FAILED: Cancelled a non-existent ticket."); // This line should not be reached
        } catch (TicketNotFoundException e) {
            System.out.println("Test Case 6 PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in Test Case 6 (unexpected exception): " + e.getMessage());
        }

        // Test Case 7: Add a show to a non-existent multiplex
        System.out.println("\n--- Test Case 7: Add show to non-existent multiplex ---");
        try {
            movieBookingService.addShowToMultiplex(
                    "Imaginary Cineplex",
                    sciFiMovie,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(3),
                    null
            );
            System.out.println("Test Case 7 FAILED: Added show to non-existent multiplex."); // Should not reach
        } catch (MultiplexNotFoundException e) {
            System.out.println("Test Case 7 PASSED: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in Test Case 7 (unexpected exception): " + e.getMessage());
        }

        System.out.println("\n--- All Important Test Cases Completed ---");
    }
}