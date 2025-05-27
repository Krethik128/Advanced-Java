package vehicelrentalsystemTests;

import com.gevernova.vehicelrentalsystem.models.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    // Test cases for Vehicle abstract class constructor (via subclasses)
    @Test
    void testVehicleConstructorValidParameters() {
        Car car = new Car("C001", "Honda", "Civic", 50.0, 5);
        assertNotNull(car);
        assertEquals("C001", car.getVehicleId());
        assertEquals("Honda", car.getBrand());
        assertEquals("Civic", car.getModel());
        assertEquals(50.0, car.getBasePricePerDay());
    }

    @Test
    void testVehicleConstructorInvalidVehicleId() {
        assertThrows(IllegalArgumentException.class, () -> new Car(null, "Honda", "Civic", 50.0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Car("", "Honda", "Civic", 50.0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Car(" ", "Honda", "Civic", 50.0, 5));
    }

    @Test
    void testVehicleConstructorInvalidBrand() {
        assertThrows(IllegalArgumentException.class, () -> new SUV("S001", null, "RAV4", 80.0, true));
        assertThrows(IllegalArgumentException.class, () -> new SUV("S001", "", "RAV4", 80.0, true));
    }

    @Test
    void testVehicleConstructorInvalidModel() {
        assertThrows(IllegalArgumentException.class, () -> new Truck("T001", "Ford", null, 120.0, 2.5));
        assertThrows(IllegalArgumentException.class, () -> new Truck("T001", "Ford", "", 120.0, 2.5));
    }

    @Test
    void testVehicleConstructorInvalidBasePrice() {
        assertThrows(IllegalArgumentException.class, () -> new Car("C001", "Honda", "Civic", 0.0, 5));
        assertThrows(IllegalArgumentException.class, () -> new SUV("S001", "Toyota", "RAV4", -10.0, true));
    }

    // Test cases for Car class
    @Test
    void testCarConstructorValidSeats() {
        Car car = new Car("C002", "Toyota", "Camry", 60.0, 4);
        assertNotNull(car);
        assertEquals(4, car.getNumberOfSeats());
        assertEquals("Car", car.getVehicleType());
    }

    @Test
    void testCarConstructorInvalidSeats() {
        assertThrows(IllegalArgumentException.class, () -> new Car("C003", "Nissan", "Altima", 55.0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Car("C004", "Nissan", "Altima", 55.0, -1));
    }

    @Test
    void testCarToString() {
        Car car = new Car("C005", "BMW", "X5", 70.0, 5);
        assertEquals("Car{id='C005', brand='BMW', model='X5', seats=5}", car.toString());
    }

    // Test cases for SUV class
    @Test
    void testSUVConstructorValid() {
        SUV suv = new SUV("S002", "Jeep", "Wrangler", 90.0, true);
        assertNotNull(suv);
        assertTrue(suv.isFourWheelDrive());
        assertEquals("SUV", suv.getVehicleType());
    }

    @Test
    void testSUVToString() {
        SUV suv = new SUV("S003", "Mercedes", "GLE", 100.0, false);
        assertEquals("SUV{id='S003', brand='Mercedes', model='GLE', 4WD=false}", suv.toString());
    }

    // Test cases for Truck class
    @Test
    void testTruckConstructorValidCapacity() {
        Truck truck = new Truck("T002", "Ram", "1500", 130.0, 3.0);
        assertNotNull(truck);
        assertEquals(3.0, truck.getCargoCapacityTons());
        assertEquals("Truck", truck.getVehicleType());
    }

    @Test
    void testTruckConstructorInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new Truck("T003", "GMC", "Sierra", 110.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Truck("T004", "GMC", "Sierra", 110.0, -0.5));
    }

    @Test
    void testTruckToString() {
        Truck truck = new Truck("T005", "Chevrolet", "Silverado", 140.0, 4.2);
        assertEquals("Truck{id='T005', brand='Chevrolet', model='Silverado', cargoCapacity=4.20 tons}", truck.toString());
    }

    // Test getVehicleType for different vehicles (OCP/LSP)
    @Test
    void testGetVehicleTypeLSP() {
        Vehicle car = new Car("C006", "Ford", "Focus", 45.0, 4);
        Vehicle suv = new SUV("S004", "Hyundai", "Tucson", 75.0, false);
        Vehicle truck = new Truck("T006", "Toyota", "Tacoma", 100.0, 1.5);

        assertEquals("Car", car.getVehicleType());
        assertEquals("SUV", suv.getVehicleType());
        assertEquals("Truck", truck.getVehicleType());
    }
}