package vehicelrentalsystemTests;



import com.gevernova.vehicelrentalsystem.services.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Booking class, focusing on its data integrity and time-based logic.
 */
public class BookingTest {

    // Test constructor with valid parameters
    @Test
    void testBookingConstructorValid() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 12, 17, 0);
        Booking booking = new Booking("B001", "V001", "C001", start, end);

        assertNotNull(booking);
        assertEquals("B001", booking.getBookingId());
        assertEquals("V001", booking.getVehicleId());
        assertEquals("C001", booking.getCustomerId());
        assertEquals(start, booking.getStartTime());
        assertEquals(end, booking.getEndTime());
        assertEquals(0.0, booking.getTotalPrice()); // Initial price should be 0.0
        assertEquals(0, booking.getCustomerRating()); // Initial rating should be 0
        assertEquals(0.0, booking.getDistanceTraveled()); // Initial distance should be 0.0
    }

    // Test constructor with invalid parameters
    @Test
    void testBookingConstructorInvalid() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 12, 17, 0);

        // Null bookingId
        assertThrows(IllegalArgumentException.class, () -> new Booking(null, "V001", "C001", start, end));
        // Empty bookingId
        assertThrows(IllegalArgumentException.class, () -> new Booking("", "V001", "C001", start, end));
        // Blank bookingId
        assertThrows(IllegalArgumentException.class, () -> new Booking(" ", "V001", "C001", start, end));

        // Null vehicleId
        assertThrows(IllegalArgumentException.class, () -> new Booking("B001", null, "C001", start, end));
        // Null customerId
        assertThrows(IllegalArgumentException.class, () -> new Booking("B001", "V001", null, start, end));
        // Null startTime
        assertThrows(IllegalArgumentException.class, () -> new Booking("B001", "V001", "C001", null, end));
        // Null endTime
        assertThrows(IllegalArgumentException.class, () -> new Booking("B001", "V001", "C001", start, null));

        // startTime after endTime
        assertThrows(IllegalArgumentException.class, () -> new Booking("B001", "V001", "C001", end, start));
    }

    // Test getDurationInDays method
    @Test
    void testGetDurationInDays() {
        // Exactly 2 days
        LocalDateTime start1 = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 1, 12, 9, 0);
        Booking booking1 = new Booking("B1", "V1", "C1", start1, end1);
        assertEquals(2, booking1.getDurationInDays());

        // 2 days and a few hours
        LocalDateTime start2 = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 1, 12, 17, 0);
        Booking booking2 = new Booking("B2", "V2", "C2", start2, end2);
        assertEquals(2, booking2.getDurationInDays()); // Should count full days

        // Less than 1 day but crosses midnight
        LocalDateTime start3 = LocalDateTime.of(2025, 1, 10, 22, 0);
        LocalDateTime end3 = LocalDateTime.of(2025, 1, 11, 2, 0);
        Booking booking3 = new Booking("B3", "V3", "C3", start3, end3);
        assertEquals(0, booking3.getDurationInDays()); // No full days, but spans two calendar days

        // Exactly 0 duration (start and end same)
        LocalDateTime start4 = LocalDateTime.of(2025, 1, 10, 10, 0);
        Booking booking4 = new Booking("B4", "V4", "C4", start4, start4);
        assertEquals(0, booking4.getDurationInDays());
    }

    // Test getDurationInHours method
    @Test
    void testGetDurationInHours() {
        // Exactly 2 days = 48 hours
        LocalDateTime start1 = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2025, 1, 12, 9, 0);
        Booking booking1 = new Booking("B1", "V1", "C1", start1, end1);
        assertEquals(48, booking1.getDurationInHours());

        // 2 days and 8 hours = 56 hours
        LocalDateTime start2 = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 1, 12, 17, 0);
        Booking booking2 = new Booking("B2", "V2", "C2", start2, end2);
        assertEquals(56, booking2.getDurationInHours());

        // Less than 1 hour
        LocalDateTime start3 = LocalDateTime.of(2025, 1, 10, 10, 0);
        LocalDateTime end3 = LocalDateTime.of(2025, 1, 10, 10, 30);
        Booking booking3 = new Booking("B3", "V3", "C3", start3, end3);
        assertEquals(0, booking3.getDurationInHours());

        // Exactly 0 duration (start and end same)
        LocalDateTime start4 = LocalDateTime.of(2025, 1, 10, 10, 0);
        Booking booking4 = new Booking("B4", "V4", "C4", start4, start4);
        assertEquals(0, booking4.getDurationInHours());
    }

    // Test overlaps method
    @Test
    void testOverlapsNoOverlapBefore() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 12, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 12, 9, 1), LocalDateTime.of(2025, 1, 14, 9, 0)); // Starts immediately after b1 ends
        assertFalse(b1.overlaps(b2));
        assertFalse(b2.overlaps(b1));
    }

    @Test
    void testOverlapsNoOverlapAfter() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 12, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 8, 9, 0), LocalDateTime.of(2025, 1, 10, 8, 59)); // Ends just before b1 starts
        assertFalse(b1.overlaps(b2));
        assertFalse(b2.overlaps(b1));
    }

    @Test
    void testOverlapsFullOverlap() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 15, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 11, 9, 0), LocalDateTime.of(2025, 1, 14, 9, 0)); // b2 fully inside b1
        assertTrue(b1.overlaps(b2));
        assertTrue(b2.overlaps(b1));
    }

    @Test
    void testOverlapsPartialOverlapStart() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 15, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 8, 9, 0), LocalDateTime.of(2025, 1, 11, 9, 0)); // b2 ends during b1
        assertTrue(b1.overlaps(b2));
        assertTrue(b2.overlaps(b1));
    }

    @Test
    void testOverlapsPartialOverlapEnd() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 15, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 14, 9, 0), LocalDateTime.of(2025, 1, 17, 9, 0)); // b2 starts during b1
        assertTrue(b1.overlaps(b2));
        assertTrue(b2.overlaps(b1));
    }

    @Test
    void testOverlapsSameStartEnd() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 12, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 12, 9, 0)); // Identical booking
        assertTrue(b1.overlaps(b2));
        assertTrue(b2.overlaps(b1));
    }

    @Test
    void testOverlapsAdjacent() {
        Booking b1 = new Booking("B1", "V1", "C1", LocalDateTime.of(2025, 1, 10, 9, 0), LocalDateTime.of(2025, 1, 12, 9, 0));
        Booking b2 = new Booking("B2", "V1", "C1", LocalDateTime.of(2025, 1, 12, 9, 0), LocalDateTime.of(2025, 1, 14, 9, 0)); // End of b1 is start of b2
        assertFalse(b1.overlaps(b2)); // They touch, but do not overlap strictly (start < end and end > start)
        assertFalse(b2.overlaps(b1));
    }

    // Test setters
    @Test
    void testSetTotalPrice() {
        Booking booking = new Booking("B001", "V001", "C001", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        booking.setTotalPrice(150.75);
        assertEquals(150.75, booking.getTotalPrice());
        assertThrows(IllegalArgumentException.class, () -> booking.setTotalPrice(-10.0));
    }

    @Test
    void testSetCustomerRating() {
        Booking booking = new Booking("B001", "V001", "C001", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        booking.setCustomerRating(3);
        assertEquals(3, booking.getCustomerRating());
        assertThrows(IllegalArgumentException.class, () -> booking.setCustomerRating(0));
        assertThrows(IllegalArgumentException.class, () -> booking.setCustomerRating(6));
    }

    @Test
    void testSetDistanceTraveled() {
        Booking booking = new Booking("B001", "V001", "C001", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        booking.setDistanceTraveled(250.5);
        assertEquals(250.5, booking.getDistanceTraveled());
        assertThrows(IllegalArgumentException.class, () -> booking.setDistanceTraveled(-50.0));
    }

    @Test
    void testBookingToString() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 11, 9, 0);
        Booking booking = new Booking("B001", "V001", "C001", start, end);
        booking.setTotalPrice(100.0);
        booking.setCustomerRating(5);
        booking.setDistanceTraveled(300.0);

        String expected = "Booking{id='B001', vehicleId='V001', customerId='C001', " +
                "startTime=2025-01-10T09:00, endTime=2025-01-11T09:00, price=100.00, rating=5, distance=300.00}";
        assertEquals(expected, booking.toString());
    }
}
