package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.exceptions.*;
import com.gevernova.movingbookingsystem.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingServices {
    private Map<String, Multiplex> multiplexes;
    private Map<String, User> users;
    private Map<String, Ticket> tickets;

    public BookingServices() {
        this.multiplexes = new HashMap<>();
        this.users = new HashMap<>();
        this.tickets = new HashMap<>();
    }

    public void addMultiplex(Multiplex multiplex) {
        if (multiplexes.containsKey(multiplex.getMultiplexName())) {
            System.out.println("Multiplex with name " + multiplex.getMultiplexName() + " already exists.");
        } else {
            multiplexes.put(multiplex.getMultiplexName(), multiplex);
            System.out.println("Multiplex " + multiplex.getMultiplexName() + " added successfully.");
        }
    }

    public Multiplex getMultiplex(String multiplexName) {
        return multiplexes.get(multiplexName);
    }

    public void addUser(User user) {
        if (users.containsKey(user.getUserId())) {
            System.out.println("User with ID " + user.getUserId() + " already exists.");
        } else {
            users.put(user.getUserId(), user);
            System.out.println("User " + user.getName() + " added successfully with ID: " + user.getUserId());
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public Show addShowToMultiplex(String multiplexName, Movie movie, LocalDateTime startTime, LocalDateTime endTime, Map<Category, Double> categoryPrices) throws ShowLimitExceeded, MultiplexNotFoundException {
        Multiplex multiplex = multiplexes.get(multiplexName);
        if (multiplex == null) {
            throw new MultiplexNotFoundException("Multiplex " + multiplexName + " not found.");
        }

        Show show = new Show(movie, startTime, endTime);
        initializeShowSeats(show, categoryPrices);

        multiplex.addShow(show, movie);
        System.out.println("Show for movie '" + movie.getMovieName() + "' added to multiplex '" + multiplexName + "' with Show ID: " + show.getShowId());
        return show;
    }

    private void initializeShowSeats(Show show, Map<Category, Double> categoryPrices) {
        int totalSeats = show.getMAX_SEATS_AVAILABLE();
        int seatsPerCategory = totalSeats / Category.values().length;

        if (categoryPrices == null || categoryPrices.size() != Category.values().length) {
            System.err.println("Warning: Category prices not fully specified. Using default prices from Category enum.");
            for (Category cat : Category.values()) {
                categoryPrices.putIfAbsent(cat, (double) cat.getPrice());
            }
        }

        int seatCounter = 1;
        for (Category category : Category.values()) {
            // double price = categoryPrices.getOrDefault(category, (double) category.getPrice()); // Price is now retrieved from Category enum directly for simplicity
            for (int i = 0; i < seatsPerCategory; i++) {
                String seatNumber = category.name() + "-" + String.format("%02d", seatCounter++);
                Seat seat = new Seat(category, seatNumber);
                // SeatStatus is initialized to AVAILABLE in Seat constructor
                show.seats.add(seat);
            }
        }
        show.setSeatsAvailable(show.seats.size());
        System.out.println("Initialized " + show.seats.size() + " seats for Show ID: " + show.getShowId());
    }

    public Optional<Show> findShow(String multiplexName, String showId) {
        Multiplex multiplex = multiplexes.get(multiplexName);
        if (multiplex != null) {
            return multiplex.getShows().stream()
                    .filter(s -> s.getShowId().equals(showId))
                    .findFirst();
        }
        return Optional.empty();
    }

    public List<Ticket> bookSeats(String userId, String multiplexName, String showId, List<String> seatNumbers)
            throws UserNotFoundException, ShowNotFoundException, SeatNotAvailableException, BookingException {

        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        Optional<Show> optionalShow = findShow(multiplexName, showId);
        if (optionalShow.isEmpty()) {
            throw new ShowNotFoundException("Show with ID " + showId + " not found in multiplex " + multiplexName + ".");
        }
        Show show = optionalShow.get();

        if (!show.isShowActive()) {
            throw new BookingException("Show " + show.getMovie().getMovieName() + " is not active for booking.");
        }
        if (show.isShowFull()) {
            throw new BookingException("Show " + show.getMovie().getMovieName() + " is full.");
        }

        List<Ticket> bookedTickets = new ArrayList<>();
        double totalBookingPrice = 0.0;

        List<Seat> seatsToBook = new ArrayList<>();
        for (String seatNumber : seatNumbers) {
            Optional<Seat> seatOptional = show.seats.stream()
                    .filter(s -> s.getSeatNumber().equals(seatNumber))
                    .findFirst();

            if (seatOptional.isEmpty()) {
                throw new SeatNotAvailableException("Seat " + seatNumber + " not found in Show " + showId + ".");
            }
            Seat seat = seatOptional.get();

            if (seat.getSeatStatus() == SeatStatus.BOOKED) { // Uses SeatStatus.BOOKED
                throw new SeatNotAvailableException("Seat " + seatNumber + " is already booked.");
            }
            seatsToBook.add(seat);
        }

        for (Seat seat : seatsToBook) {
            seat.setSeatStatus(SeatStatus.BOOKED); // Sets SeatStatus to BOOKED
            Ticket ticket = new Ticket(seat.getSeatNumber(), show.getShowId());
            tickets.put(ticket.getTicketId(), ticket);
            bookedTickets.add(ticket);

            totalBookingPrice += seat.getCategory().getPrice();
            show.setSeatsAvailable(show.getSeatsAvailable() - 1);
        }

        if (show.getSeatsAvailable() == 0) {
            show.setShowFull(true);
        }

        System.out.println("Successfully booked " + bookedTickets.size() + " tickets for user " + user.getName() +
                " for show '" + show.getMovie().getMovieName() + "'. Total price: $" + totalBookingPrice);
        return bookedTickets;
    }

    public boolean cancelTicket(String ticketId) throws TicketNotFoundException {
        Ticket ticket = tickets.get(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket with ID " + ticketId + " not found.");
        }

        Optional<Multiplex> multiplexContainingShow = multiplexes.values().stream()
                .filter(m -> m.isShowPresent(ticket.getShowId()))
                .findFirst();

        if (multiplexContainingShow.isEmpty()) {
            System.err.println("Error: Associated multiplex for ticket " + ticketId + " not found.");
            return false;
        }

        Optional<Show> showOptional = multiplexContainingShow.get().getShowById(ticket.getShowId());

        if (showOptional.isEmpty()) {
            System.err.println("Error: Associated show for ticket " + ticketId + " not found.");
            return false;
        }
        Show show = showOptional.get();

        Optional<Seat> seatOptional = show.seats.stream()
                .filter(s -> s.getSeatNumber().equals(ticket.getSeatId()))
                .findFirst();

        if (seatOptional.isEmpty()) {
            System.err.println("Error: Associated seat for ticket " + ticketId + " not found in show " + show.getShowId() + ".");
            return false;
        }
        Seat seat = seatOptional.get();

        seat.setSeatStatus(SeatStatus.AVAILABLE); // Sets SeatStatus to AVAILABLE
        show.setSeatsAvailable(show.getSeatsAvailable() + 1);
        show.setShowFull(false);

        tickets.remove(ticketId);
        System.out.println("Ticket " + ticketId + " cancelled successfully. Seat " + seat.getSeatNumber() + " is now available.");
        return true;
    }

    public List<Seat> getAvailableSeats(String multiplexName, String showId) throws ShowNotFoundException {
        Optional<Show> optionalShow = findShow(multiplexName, showId);
        if (optionalShow.isEmpty()) {
            throw new ShowNotFoundException("Show with ID " + showId + " not found in multiplex " + multiplexName + ".");
        }
        Show show = optionalShow.get();

        return show.seats.stream()
                .filter(seat -> seat.getSeatStatus() == SeatStatus.AVAILABLE) // Filters by SeatStatus.AVAILABLE
                .collect(Collectors.toList());
    }

    public List<Seat> getBookedSeats(String multiplexName, String showId) throws ShowNotFoundException {
        Optional<Show> optionalShow = findShow(multiplexName, showId);
        if (optionalShow.isEmpty()) {
            throw new ShowNotFoundException("Show with ID " + showId + " not found in multiplex " + multiplexName + ".");
        }
        Show show = optionalShow.get();

        return show.seats.stream()
                .filter(seat -> seat.getSeatStatus() == SeatStatus.BOOKED) // Filters by SeatStatus.BOOKED
                .collect(Collectors.toList());
    }

    public Map<String, Multiplex> getMultiplexes() {
        return multiplexes;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Ticket> getTickets() {
        return tickets;
    }
}