package moviebookingservicesTests;

import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.model.Screen;
import com.gevernova.movingbookingsystem.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiplexManagementServiceTest {

    private MultiplexManagementService multiplexManagementService;

    private String multiplexName;
    private String multiplexLocation;
    private String screenName;
    private int screenRows;
    private int screenSeatsPerRow;

    @BeforeEach
    void setUp() {
        multiplexManagementService = new MultiplexManagementService();
        multiplexName = "Grand Cinema";
        multiplexLocation = "Downtown";
        screenName = "Screen One";
        screenRows = 5;
        screenSeatsPerRow = 10;
    }

    @Test
    @DisplayName("Add multiplex successfully")
    void addMultiplexSuccessfully() {
        Multiplex multiplex = multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        assertNotNull(multiplex);
        assertEquals(multiplexName, multiplex.getName());
        assertEquals(multiplexLocation, multiplex.getAddress());
        assertNotNull(multiplex.getId());
    }

    @Test
    @DisplayName("Add multiplex fails if name already exists")
    void addMultiplexFailsIfNameExists() {
        multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            multiplexManagementService.addMultiplex(multiplexName, "Another Location");
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Get multiplex by ID successfully")
    void getMultiplexByIdSuccessfully() {
        Multiplex multiplex = multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        Multiplex foundMultiplex = multiplexManagementService.getMultiplexById(multiplex.getId());
        assertNotNull(foundMultiplex);
        assertEquals(multiplex.getId(), foundMultiplex.getId());
    }

    @Test
    @DisplayName("Get multiplex by ID returns null for non-existent ID")
    void getMultiplexByIdReturnsNullForNonExistentId() {
        Multiplex multiplex = multiplexManagementService.getMultiplexById("nonExistentId");
        assertNull(multiplex);
    }

    @Test
    @DisplayName("Add screen to multiplex successfully")
    void addScreenToMultiplexSuccessfully() {
        Multiplex multiplex = multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        Screen screen = multiplexManagementService.addScreenToMultiplex(multiplex.getId(), screenName, screenRows, screenSeatsPerRow);
        assertNotNull(screen);
        assertEquals(screenName, screen.getName());
        assertTrue(multiplex.getScreens().contains(screen));
    }

    @Test
    @DisplayName("Add screen fails if multiplex ID does not exist")
    void addScreenFailsIfMultiplexIdDoesNotExist() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            multiplexManagementService.addScreenToMultiplex("nonExistentId", screenName, screenRows, screenSeatsPerRow);
        });
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Add screen fails if screen name already exists in multiplex")
    void addScreenFailsIfScreenNameExistsInMultiplex() {
        Multiplex multiplex = multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        multiplexManagementService.addScreenToMultiplex(multiplex.getId(), screenName, screenRows, screenSeatsPerRow);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            multiplexManagementService.addScreenToMultiplex(multiplex.getId(), screenName, screenRows, screenSeatsPerRow);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Get screen by ID successfully")
    void getScreenByIdSuccessfully() {
        Multiplex multiplex = multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        Screen screen = multiplexManagementService.addScreenToMultiplex(multiplex.getId(), screenName, screenRows, screenSeatsPerRow);
        Screen foundScreen = multiplexManagementService.getScreenFromMultiplex(multiplex.getId(), screen.getId());
        assertNotNull(foundScreen);
        assertEquals(screen.getId(), foundScreen.getId());
    }

    @Test
    @DisplayName("Get screen by ID returns null if multiplex not found")
    void getScreenByIdReturnsNullIfMultiplexNotFound() {
        Screen screen = multiplexManagementService.getScreenFromMultiplex("nonExistentMultiplexId", "anyScreenId");
        assertNull(screen);
    }

    @Test
    @DisplayName("Get screen by ID returns null if screen not found")
    void getScreenByIdReturnsNullIfScreenNotFound() {
        Multiplex multiplex = multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        Screen screen = multiplexManagementService.getScreenFromMultiplex(multiplex.getId(), "nonExistentScreenId");
        assertNull(screen);
    }

    @Test
    @DisplayName("Get all multiplexes returns list of multiplexes")
    void getAllMultiplexesReturnsListOfMultiplexes() {
        multiplexManagementService.addMultiplex(multiplexName, multiplexLocation);
        multiplexManagementService.addMultiplex("Cinema World", "Uptown");
        List<Multiplex> multiplexes = multiplexManagementService.getAllMultiplexes();
        assertEquals(2, multiplexes.size());
    }
}
