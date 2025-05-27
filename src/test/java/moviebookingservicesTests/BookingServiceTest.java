package moviebookingservicesTests;

import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    private UserService userService;
    private MovieService movieService;
    private CategoryService categoryService;
    private MultiplexManagementService multiplexManagementService;
    private ShowSchedulingService showSchedulingService;
    private BookingService bookingService;
    private TicketService ticketService;

    private User user;
    private Movie movie;
    private Multiplex multiplex;
    private Screen screen;
    private Show show;
    private PaymentDetails paymentDetails;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService();
        movieService = new MovieService(categoryService);
        multiplexManagementService = new MultiplexManagementService();
        showSchedulingService = new ShowSchedulingService(movieService, multiplexManagementService);
        userService = new UserService();
        bookingService = new BookingService(showSchedulingService, userService);

        Category category = categoryService.addCategory("Action");
        movie = movieService.addMovie("Test Movie", "Description", 120, category.getName());
        multiplex = multiplexManagementService.addMultiplex("Test Multiplex", "Test Location");
        screen = multiplexManagementService.addScreenToMultiplex(multiplex.getId(), "Test Screen", 5, 5);
        show = showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)));
        user = userService.registerUser("testuser", "password", "testuser@example.com");
        paymentDetails = new PaymentDetails("Credit Card", 100.0, "TXN123");
        ticketService = new TicketService();
    }

    @Test
    @DisplayName("Get available seats returns all seats initially")
    void getAvailableSeatsReturnsAllSeatsInitially() {
        List<SeatStatus> seatStatuses = bookingService.getAvailableSeats(show.getId());
        assertNotNull(seatStatuses);
        assertEquals(screen.getSeats().size(), seatStatuses.size());
        assertTrue(seatStatuses.stream().allMatch(s -> s.getStatus() == SeatStatus.Status.AVAILABLE));
    }

    @Test
    @DisplayName("Book seats successfully")
    void bookSeatsSuccessfully() {
        List<SeatStatus> seatStatuses = bookingService.getAvailableSeats(show.getId());
        List<String> seatIdsToBook = List.of(seatStatuses.get(0).getSeat().getId(), seatStatuses.get(1).getSeat().getId());

        List<Ticket> tickets = bookingService.bookSeats(show.getId(), seatIdsToBook, user.getUsername(), paymentDetails);
        assertEquals(seatIdsToBook.size(), tickets.size());

        // Verify booked seats are marked booked
        List<SeatStatus> updatedSeatStatuses = bookingService.getAvailableSeats(show.getId());
        long bookedCount = updatedSeatStatuses.stream()
                .filter(s -> seatIdsToBook.contains(s.getSeat().getId()) && s.getStatus() == SeatStatus.Status.BOOKED)
                .count();
        assertEquals(seatIdsToBook.size(), bookedCount);
    }

    @Test
    @DisplayName("Book seats fails if seat already booked")
    void bookSeatsFailsIfSeatAlreadyBooked() {
        List<SeatStatus> seatStatuses = bookingService.getAvailableSeats(show.getId());
        String seatId = seatStatuses.get(0).getSeat().getId();

        bookingService.bookSeats(show.getId(), List.of(seatId), user.getUsername(), paymentDetails);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.bookSeats(show.getId(), List.of(seatId), user.getUsername(), paymentDetails);
        });
        assertTrue(exception.getMessage().contains("already booked"));
    }

    @Test
    @DisplayName("Get tickets for user returns booked tickets")
    void getTicketsForUserReturnsBookedTickets() {
        List<SeatStatus> seatStatuses = bookingService.getAvailableSeats(show.getId());
        List<String> seatIdsToBook = List.of(seatStatuses.getFirst().getSeat().getId());

        bookingService.bookSeats(show.getId(), seatIdsToBook, user.getUsername(), paymentDetails);

        List<Ticket> tickets = ticketService.getTicketsForUser(user.getId());
        assertFalse(tickets.isEmpty());
        assertEquals(user.getId(), tickets.getFirst().getUser().getId());
    }

    @Test
    @DisplayName("Get ticket by ID returns correct ticket")
    void getTicketByIdReturnsCorrectTicket() {
        List<SeatStatus> seatStatuses = bookingService.getAvailableSeats(show.getId());
        List<String> seatIdsToBook = List.of(seatStatuses.get(0).getSeat().getId());

        List<Ticket> tickets = bookingService.bookSeats(show.getId(), seatIdsToBook, user.getUsername(), paymentDetails);
        Ticket ticket = tickets.get(0);

        Ticket foundTicket = ticketService.getTicketById(ticket.getId());
        assertNotNull(foundTicket);
        assertEquals(ticket.getId(), foundTicket.getId());
    }

    @Test
    @DisplayName("Get ticket by ID returns null for non-existent ticket")
    void getTicketByIdReturnsNullForNonExistentTicket() {
        Ticket ticket = ticketService.getTicketById("NonExistentUserId");
        assertNull(ticket);
    }
}

