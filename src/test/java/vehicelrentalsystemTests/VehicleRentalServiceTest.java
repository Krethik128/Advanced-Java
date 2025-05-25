package vehicelrentalsystemTests;

import com.gevernova.vehicelrentalsystem.services.PerDayPricingStrategy;
import com.gevernova.vehicelrentalsystem.services.PerHourPricingStrategy;
import com.gevernova.vehicelrentalsystem.services.VehicleRentalService;
import com.gevernova.vehicelrentalsystem.models.Booking;
import com.gevernova.vehicelrentalsystem.models.Car;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class VehicleRentalServiceTest {
    private VehicleRentalService service;

    @BeforeEach
    void setUp() {
        service = new VehicleRentalService();
        service.addVehicle(new Car("CAR001", "Toyota", "Camry", 50.0));
    }

    @Test
    void testCreateBooking() throws Exception {
        Booking booking = service.createBooking("B001", "CAR001", "CUST001",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertNotNull(booking);
        assertEquals(50.0, booking.getTotalPrice());
    }

    @Test
    void testOverlappingBooking() {
        assertThrows(Exception.class, () -> {
            LocalDateTime start = LocalDateTime.now();
            service.createBooking("B001", "CAR001", "CUST001", start, start.plusHours(2));
            service.createBooking("B002", "CAR001", "CUST002", start.plusHours(1), start.plusHours(3));
        });
    }

    @Test
    void testPricingStrategies() throws Exception {
        // Test per-day pricing
        service.setPricingStrategy(new PerDayPricingStrategy());
        Booking dayBooking = service.createBooking("B001", "CAR001", "CUST001",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        // Test per-hour pricing
        service.setPricingStrategy(new PerHourPricingStrategy());
        Booking hourBooking = service.createBooking("B002", "CAR001", "CUST002",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1));

        assertTrue(dayBooking.getTotalPrice() > hourBooking.getTotalPrice());
    }
}
