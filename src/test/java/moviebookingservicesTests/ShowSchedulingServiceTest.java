package moviebookingservicesTests;

import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.services.*;
import com.gevernova.movingbookingsystem.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ShowSchedulingServiceTest {

    private MovieService movieService;
    private MultiplexManagementService multiplexManagementService;
    private ShowSchedulingService showSchedulingService;

    private Movie movie;
    private Multiplex multiplex;
    private Screen screen;
    private Date showTime;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(new CategoryService());
        multiplexManagementService = new MultiplexManagementService();
        showSchedulingService = new ShowSchedulingService(movieService, multiplexManagementService);
        CategoryService categoryService = new CategoryService();
        Category actionCategory = categoryService.addCategory("Action");
        movie = movieService.addMovie("Test Movie", "Test Description", 120, actionCategory.getName());

        multiplex = multiplexManagementService.addMultiplex("Test Multiplex", "Test Location");
        screen = multiplexManagementService.addScreenToMultiplex(multiplex.getId(), "Test Screen", 5, 10);

        showTime = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2));
    }

    @Test
    @DisplayName("Schedule show successfully")
    void scheduleShowSuccessfully() {
        Show show = showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), showTime);
        assertNotNull(show);
        assertEquals(movie.getId(), show.getMovie().getId());
        assertEquals(multiplex.getId(), show.getMultiplex().getId());
        assertEquals(screen.getId(), show.getScreen().getId());
        assertEquals(showTime, show.getShowTime());
    }

    @Test
    @DisplayName("Schedule show fails if movie not found")
    void scheduleShowFailsIfMovieNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            showSchedulingService.scheduleShow("nonExistentMovieId", multiplex.getId(), screen.getId(), showTime);
        });
        assertTrue(exception.getMessage().contains("Movie with ID"));
    }

    @Test
    @DisplayName("Schedule show fails if multiplex not found")
    void scheduleShowFailsIfMultiplexNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            showSchedulingService.scheduleShow(movie.getId(), "nonExistentMultiplexId", screen.getId(), showTime);
        });
        assertTrue(exception.getMessage().contains("Multiplex with ID"));
    }

    @Test
    @DisplayName("Schedule show fails if screen not found")
    void scheduleShowFailsIfScreenNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), "nonExistentScreenId", showTime);
        });
        assertTrue(exception.getMessage().contains("Screen with ID"));
    }

    @Test
    @DisplayName("Schedule show fails due to overlapping show")
    void scheduleShowFailsDueToOverlappingShow() {
        showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), showTime);
        Date overlappingTime = new Date(showTime.getTime() + TimeUnit.MINUTES.toMillis(30));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), overlappingTime);
        });
        assertTrue(exception.getMessage().contains("overlaps"));
    }

    @Test
    @DisplayName("Get show by ID successfully")
    void getShowByIdSuccessfully() {
        Show show = showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), showTime);
        Show foundShow = showSchedulingService.getShowById(show.getId());
        assertNotNull(foundShow);
        assertEquals(show.getId(), foundShow.getId());
    }

    @Test
    @DisplayName("Get shows by movie ID returns correct shows")
    void getShowsByMovieIdReturnsCorrectShows() {
        showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), showTime);
        List<Show> shows = showSchedulingService.getShowsByMovieId(movie.getId());
        assertFalse(shows.isEmpty());
        assertEquals(movie.getId(), shows.get(0).getMovie().getId());
    }

    @Test
    @DisplayName("Get shows by multiplex ID returns correct shows")
    void getShowsByMultiplexIdReturnsCorrectShows() {
        showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), showTime);
        List<Show> shows = showSchedulingService.getShowsByMultiplexId(multiplex.getId());
        assertFalse(shows.isEmpty());
        assertEquals(multiplex.getId(), shows.get(0).getMultiplex().getId());
    }

    @Test
    @DisplayName("Get all shows returns list of shows")
    void getAllShowsReturnsListOfShows() {
        showSchedulingService.scheduleShow(movie.getId(), multiplex.getId(), screen.getId(), showTime);
        List<Show> shows = showSchedulingService.getAllShows();
        assertFalse(shows.isEmpty());
    }
}

