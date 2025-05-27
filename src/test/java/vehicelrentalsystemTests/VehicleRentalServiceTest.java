package vehicelrentalsystemTests;

import com.gevernova.vehicelrentalsystem.exceptionhandling.*;
import com.gevernova.vehicelrentalsystem.models.*;
import com.gevernova.vehicelrentalsystem.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VehicleRentalService, focusing on its role as an orchestrator
 * and adherence to SRP (delegating booking logic to BookingService).
 */
public class VehicleRentalServiceTest {

    private VehicleRentalService rentalService;
    private Car testCar;
    private SUV testSUV;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Initialize with a default pricing strategy for the internal BookingService
        rentalService = new VehicleRentalService(new PerDayPricingStrategy());
        testCar = new Car("C001", "Honda", "Civic", 50.0, 5);
        testSUV = new SUV("S001", "Toyota", "RAV4", 80.0, true);
        now = LocalDateTime.now();

        // Add vehicles to the service for most tests
        rentalService.addVehicle(testCar);
        rentalService.addVehicle(testSUV);
    }

    // --- Add Vehicle Tests ---
    @Test
    void testAddVehicle_Success() {
        Car newCar = new Car("C002", "Ford", "Focus", 40.0, 4);
        assertDoesNotThrow(() -> rentalService.addVehicle(newCar));
        assertNotNull(rentalService.getVehicle("C002"));
        assertEquals(3, rentalService.getAllVehicles().size());
    }

    @Test
    void testAddVehicle_DuplicateId() {
        // Attempt to add testCar again
        assertThrows(IllegalArgumentException.class, () -> rentalService.addVehicle(testCar));
        assertEquals(2, rentalService.getAllVehicles().size()); // Size should remain 2
    }

    @Test
    void testAddVehicle_NullVehicle() {
        assertThrows(IllegalArgumentException.class, () -> rentalService.addVehicle(null));
    }

    @Test
    void testAddVehicle_NullVehicleId() {
        Car invalidCar = new Car(null, "Brand", "Model", 30.0, 4);
        assertThrows(IllegalArgumentException.class, () -> rentalService.addVehicle(invalidCar));
    }

    // --- Get Vehicle Tests ---
    @Test
    void testGetVehicle_Found() {
        Vehicle foundVehicle = rentalService.getVehicle(testCar.getVehicleId());
        assertNotNull(foundVehicle);
        assertEquals(testCar.getVehicleId(), foundVehicle.getVehicleId());
    }

    @Test
    void testGetVehicle_NotFound() {
        Vehicle foundVehicle = rentalService.getVehicle("NON_EXISTENT_ID");
        assertNull(foundVehicle);
    }

    @Test
    void testGetAllVehicles() {
        Collection<Vehicle> allVehicles = rentalService.getAllVehicles();
        assertNotNull(allVehicles);
        assertEquals(2, allVehicles.size());
        assertTrue(allVehicles.contains(testCar));
        assertTrue(allVehicles.contains(testSUV));
    }

    // --- Create Booking Tests ---
    @Test
    void testCreateBooking_SuccessPerDayPricing() throws BookingOverlapException, IllegalArgumentException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3); // 2 days
        Booking booking = rentalService.createBooking("B001", testCar.getVehicleId(), "Cust001", start, end);

        assertNotNull(booking);
        assertEquals("B001", booking.getBookingId());
        assertEquals(testCar.getVehicleId(), booking.getVehicleId());
        assertEquals(50.0 * 2, booking.getTotalPrice(), 0.001); // 50 * 2 days = 100.0
    }

    @Test
    void testCreateBooking_SuccessPerHourPricing() throws BookingOverlapException, IllegalArgumentException {
        // Re-initialize rental service with PerHourPricingStrategy
        rentalService = new VehicleRentalService(new PerHourPricingStrategy());
        rentalService.addVehicle(testCar); // Add vehicle again for this new instance

        LocalDateTime start = now.plusHours(1);
        LocalDateTime end = now.plusHours(5); // 4 hours
        Booking booking = rentalService.createBooking("B002", testCar.getVehicleId(), "Cust002", start, end);

        assertNotNull(booking);
        assertEquals("B002", booking.getBookingId());
        assertEquals(testCar.getVehicleId(), booking.getVehicleId());
        // (50.0 / 24.0) * 4 hours = 8.333...
        assertEquals((50.0 / 24.0) * 4, booking.getTotalPrice(), 0.001);
    }

    @Test
    void testCreateBooking_VehicleNotFound() {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        assertThrows(IllegalArgumentException.class, () ->
                rentalService.createBooking("B003", "NON_EXISTENT_V", "Cust003", start, end));
    }

    @Test
    void testCreateBooking_BookingOverlap() throws BookingOverlapException, IllegalArgumentException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(3);
        rentalService.createBooking("B004", testCar.getVehicleId(), "Cust004", start1, end1);

        LocalDateTime startOverlap = now.plusDays(2);
        LocalDateTime endOverlap = now.plusDays(4);
        assertThrows(BookingOverlapException.class, () ->
                rentalService.createBooking("B005", testCar.getVehicleId(), "Cust005", startOverlap, endOverlap));
    }

    // --- Complete Booking Tests ---
    @Test
    void testCompleteBooking_Success() throws BookingOverlapException, InvalidRatingException, IllegalArgumentException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        rentalService.createBooking("B006", testCar.getVehicleId(), "Cust006", start, end);

        assertDoesNotThrow(() -> rentalService.completeBooking("B006", 5, 200.0));
        Booking completedBooking = rentalService.getBookingsForVehicle(testCar.getVehicleId()).get(0);
        assertEquals(5, completedBooking.getCustomerRating());
        assertEquals(200.0, completedBooking.getDistanceTraveled());
    }

    @Test
    void testCompleteBooking_InvalidRating() throws BookingOverlapException, IllegalArgumentException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(3);
        rentalService.createBooking("B007", testCar.getVehicleId(), "Cust007", start, end);

        assertThrows(InvalidRatingException.class, () -> rentalService.completeBooking("B007", 0, 100.0));
        assertThrows(InvalidRatingException.class, () -> rentalService.completeBooking("B007", 6, 100.0));
    }

    @Test
    void testCompleteBooking_BookingNotFound() {
        assertThrows(IllegalArgumentException.class, () -> rentalService.completeBooking("NON_EXISTENT_B", 4, 100.0));
    }

    // --- Get Bookings For Vehicle Tests ---
    @Test
    void testGetBookingsForVehicle_Existing() throws BookingOverlapException, IllegalArgumentException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(2);
        rentalService.createBooking("B008", testCar.getVehicleId(), "Cust008", start1, end1);

        LocalDateTime start2 = now.plusDays(3);
        LocalDateTime end2 = now.plusDays(4);
        rentalService.createBooking("B009", testCar.getVehicleId(), "Cust009", start2, end2);

        List<Booking> bookings = rentalService.getBookingsForVehicle(testCar.getVehicleId());
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals("B008")));
        assertTrue(bookings.stream().anyMatch(b -> b.getBookingId().equals("B009")));
    }

    @Test
    void testGetBookingsForVehicle_None() {
        List<Booking> bookings = rentalService.getBookingsForVehicle("NON_EXISTENT_V");
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }

    // --- Get Average Rating Tests ---
    @Test
    void testGetAverageRating_NoRatings() throws BookingOverlapException, IllegalArgumentException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);
        rentalService.createBooking("B010", testCar.getVehicleId(), "Cust010", start, end); // Not completed

        assertEquals(0.0, rentalService.getAverageRating(testCar.getVehicleId()), 0.001);
    }

    @Test
    void testGetAverageRating_WithRatings() throws BookingOverlapException, InvalidRatingException, IllegalArgumentException {
        LocalDateTime start1 = now.plusDays(1);
        LocalDateTime end1 = now.plusDays(2);
        rentalService.createBooking("B011", testCar.getVehicleId(), "Cust011", start1, end1);
        rentalService.completeBooking("B011", 3, 100.0);

        LocalDateTime start2 = now.plusDays(3);
        LocalDateTime end2 = now.plusDays(4);
        rentalService.createBooking("B012", testCar.getVehicleId(), "Cust012", start2, end2);
        rentalService.completeBooking("B012", 5, 150.0);

        assertEquals(4.0, rentalService.getAverageRating(testCar.getVehicleId()), 0.001); // (3+5)/2
    }

    @Test
    void testGetAverageRating_DifferentVehicle() throws BookingOverlapException, InvalidRatingException, IllegalArgumentException {
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);
        rentalService.createBooking("B013", testSUV.getVehicleId(), "Cust013", start, end);
        rentalService.completeBooking("B013", 4, 200.0);

        assertEquals(4.0, rentalService.getAverageRating(testSUV.getVehicleId()), 0.001);
        assertEquals(0.0, rentalService.getAverageRating(testCar.getVehicleId()), 0.001);
    }
}
