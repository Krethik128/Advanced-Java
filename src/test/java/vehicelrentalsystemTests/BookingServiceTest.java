package vehicelrentalsystemTests;

import com.gevernova.vehicelrentalsystem.exceptionhandling.*;
import com.gevernova.vehicelrentalsystem.models.Car;
import com.gevernova.vehicelrentalsystem.models.Vehicle;
import com.gevernova.vehicelrentalsystem.services.*;
import com.gevernova.vehicelrentalsystem.services.BookingService;
import com.gevernova.vehicelrentalsystem.services.PerDayPricingStrategy;
import com.gevernova.vehicelrentalsystem.services.PricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the BookingService class, ensuring SRP compliance and correct booking logic.
 */
public class BookingServiceTest {

    private BookingService bookingService;
    private PricingStrategy pricingStrategy;
    private Vehicle testCar;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize with PerDayPricingStrategy for consistent pricing tests
        pricingStrategy = new PerDayPricingStrategy();
        bookingService = new BookingService(pricingStrategy);
        testCar = new Car("C001", "Honda", "Civic", 50.0, 5); // Base price $50/day
        now = LocalDateTime.now();
    }

    // --- Create Booking Tests ---
    @Test
    void testCreateBooking_Success() throws BookingOverlapException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3); // 2 days
        Booking booking = bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end, testCar);

        assertNotNull(booking);
        assertEquals("B001", booking.getBookingId());
        assertEquals(testCar.getVehicleId(), booking.getVehicleId());
        assertEquals(50.0 * 2, booking.getTotalPrice(), 0.001); // 50 * 2 days
        assertEquals(1, bookingService.getBookingsForVehicle(testCar.getVehicleId()).size());
    }

    @Test
    void testCreateBooking_OverlapException() throws BookingOverlapException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(3);
        bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start1, end1, testCar);

        // Attempt to create an overlapping booking
        LocalDateTime startOverlap = now.plusDays(2);
        LocalDateTime endOverlap = now.plusDays(4);
        assertThrows(BookingOverlapException.class, () ->
                bookingService.createBooking("B002", testCar.getVehicleId(), "Cust002", startOverlap, endOverlap, testCar));
    }

    @Test
    void testCreateBooking_NoOverlapAdjacent() throws BookingOverlapException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(3);
        bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start1, end1, testCar);

        // Booking starts exactly when previous one ends (no overlap)
        LocalDateTime startNoOverlap = now.plusDays(3);
        LocalDateTime endNoOverlap = now.plusDays(5);
        assertDoesNotThrow(() ->
                bookingService.createBooking("B002", testCar.getVehicleId(), "Cust002", startNoOverlap, endNoOverlap, testCar));
        assertEquals(2, bookingService.getBookingsForVehicle(testCar.getVehicleId()).size());
    }

    @Test
    void testCreateBooking_IllegalArgumentException_NullVehicle() {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking("B001", "V001", "Cust001", start, end, null));
    }

    @Test
    void testCreateBooking_IllegalArgumentException_InvalidBookingParams() {
        // Invalid booking parameters (e.g., end time before start time) are caught by Booking constructor
        LocalDateTime start = now.plusDays(3);
        LocalDateTime end = now.plusDays(1);
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end, testCar));
    }

    // --- Complete Booking Tests ---
    @Test
    void testCompleteBooking_Success() throws BookingOverlapException, InvalidRatingException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        Booking booking = bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end, testCar);

        bookingService.completeBooking("B001", 5, 250.0);
        assertEquals(5, booking.getCustomerRating());
        assertEquals(250.0, booking.getDistanceTraveled());
    }

    @Test
    void testCompleteBooking_InvalidRatingException_TooLow() {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        assertDoesNotThrow(() -> bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end, testCar));

        assertThrows(InvalidRatingException.class, () ->
                bookingService.completeBooking("B001", 0, 100.0));
    }

    @Test
    void testCompleteBooking_InvalidRatingException_TooHigh() {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        assertDoesNotThrow(() -> bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end, testCar));

        assertThrows(InvalidRatingException.class, () ->
                bookingService.completeBooking("B001", 6, 100.0));
    }

    @Test
    void testCompleteBooking_IllegalArgumentException_BookingNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.completeBooking("NON_EXISTENT_B", 4, 100.0));
    }

    @Test
    void testCompleteBooking_IllegalArgumentException_NegativeDistance() throws BookingOverlapException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end, testCar);

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.completeBooking("B001", 4, -10.0));
    }

    // --- Get Bookings For Vehicle Tests ---
    @Test
    void testGetBookingsForVehicle_ExistingBookings() throws BookingOverlapException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(2);
        bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start1, end1, testCar);

        LocalDateTime start2 = now.plusDays(3);
        LocalDateTime end2 = now.plusDays(4);
        bookingService.createBooking("B002", testCar.getVehicleId(), "Cust002", start2, end2, testCar);

        List<Booking> bookings = bookingService.getBookingsForVehicle(testCar.getVehicleId());
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals("B001")));
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals("B002")));
    }

    @Test
    void testGetBookingsForVehicle_NoBookings() {
        List<Booking> bookings = bookingService.getBookingsForVehicle("NON_EXISTENT_V");
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }

    // --- Get Average Rating Tests ---
    @Test
    void testGetAverageRating_NoRatedBookings() {
        // No bookings yet, or bookings not completed with rating
        assertEquals(0.0, bookingService.getAverageRating(testCar.getVehicleId()), 0.001);
    }

    @Test
    void testGetAverageRating_SingleRatedBooking() throws BookingOverlapException, InvalidRatingException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(2);
        bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start1, end1, testCar);
        bookingService.completeBooking("B001", 4, 100.0);

        assertEquals(4.0, bookingService.getAverageRating(testCar.getVehicleId()), 0.001);
    }

    @Test
    void testGetAverageRating_MultipleRatedBookings() throws BookingOverlapException, InvalidRatingException {
        // First booking
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(2);
        bookingService.createBooking("B001", testCar.getVehicleId(), "Cust001", start1, end1, testCar);
        bookingService.completeBooking("B001", 4, 100.0);

        // Second booking
        LocalDateTime start2 = now.plusDays(3);
        LocalDateTime end2 = now.plusDays(4);
        bookingService.createBooking("B002", testCar.getVehicleId(), "Cust002", start2, end2, testCar);
        bookingService.completeBooking("B002", 5, 150.0);

        // Third booking (unrated)
        LocalDateTime start3 = now.plusDays(5);
        LocalDateTime end3 = now.plusDays(6);
        bookingService.createBooking("B003", testCar.getVehicleId(), "Cust003", start3, end3, testCar);
        // Not completed with rating

        assertEquals(4.5, bookingService.getAverageRating(testCar.getVehicleId()), 0.001); // (4 + 5) / 2
    }

    @Test
    void testGetAverageRating_AnotherVehicle() throws BookingOverlapException, InvalidRatingException {
        Vehicle anotherCar = new Car("C002", "Ford", "Focus", 40.0, 4);
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);
        bookingService.createBooking("B004", anotherCar.getVehicleId(), "Cust004", start, end, anotherCar);
        bookingService.completeBooking("B004", 3, 80.0);

        assertEquals(3.0, bookingService.getAverageRating(anotherCar.getVehicleId()), 0.001);
        assertEquals(0.0, bookingService.getAverageRating(testCar.getVehicleId()), 0.001); // Test car should still be 0 if no ratings
    }
}
