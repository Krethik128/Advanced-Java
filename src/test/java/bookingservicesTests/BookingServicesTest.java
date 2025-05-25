package bookingservicesTests;

import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.services.BookingServices;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.*;

class BookingServicesTest {

    BookingServices bookingServices;
    Multiplex multiplex;
    User user;
    Movie movie;

    @BeforeEach
    void setUp() throws Exception {
        bookingServices = new BookingServices();
        multiplex = new Multiplex("PVR");
        bookingServices.addMultiplex(multiplex);
        user = new User("Alice", "alice@example.com", "1234567890");
        bookingServices.addUser(user);
        movie = new Movie("Inception", 120);
    }

    @Test
    void testAddShowAndBookSeats() throws Exception {
        Map<Category, Double> prices = new HashMap<>();
        for (Category cat : Category.values()) prices.put(cat, 200.0);
        Show show = bookingServices.addShowToMultiplex("PVR", movie, LocalDateTime.now(), LocalDateTime.now().plusHours(2), prices);

        List<String> seatNumbers = Arrays.asList("REGULAR-01", "REGULAR-02");
        List<Ticket> tickets = bookingServices.bookSeats(user.getUserId(), "PVR", show.getShowId(), seatNumbers);

        Assertions.assertEquals(2, tickets.size());
        Assertions.assertEquals(98, show.getSeatsAvailable());
    }

    @Test
    void testBookAlreadyBookedSeatThrowsException() throws Exception {
        // ... similar setup as above ...
        // Book seat
        // Try booking same seat again and expect SeatNotAvailableException
    }

    // More tests for cancellation, invalid user, etc.
}
