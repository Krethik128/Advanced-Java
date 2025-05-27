package moviebookingservicesTests;

import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.model.Movie;
import com.gevernova.movingbookingsystem.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    private CategoryService categoryService;
    private MovieService movieService;

    private String adventureCategoryName;
    private String comedyCategoryName;

    private String movieOneTitle;
    private String movieOneDescription;
    private int movieOneDuration;

    private String movieTwoTitle;
    private String movieTwoDescription;
    private int movieTwoDuration;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService();
        movieService = new MovieService(categoryService);

        adventureCategoryName = "Adventure";
        comedyCategoryName = "Comedy";

        movieOneTitle = "Epic Quest";
        movieOneDescription = "An epic adventure";
        movieOneDuration = 130;

        movieTwoTitle = "Laugh Out Loud";
        movieTwoDescription = "A hilarious comedy";
        movieTwoDuration = 95;
    }

    @Test
    @DisplayName("Add movie successfully with existing category")
    void addMovieSuccessfullyWithExistingCategory() {
        categoryService.addCategory(adventureCategoryName);
        Movie newMovie = movieService.addMovie(movieOneTitle, movieOneDescription, movieOneDuration, adventureCategoryName);
        assertNotNull(newMovie);
        assertEquals(movieOneTitle, newMovie.getTitle());
        assertEquals(adventureCategoryName, newMovie.getCategory());
    }


    @Test
    @DisplayName("Add movie fails if title already exists")
    void addMovieFailsIfTitleAlreadyExists() {
        movieService.addMovie(movieOneTitle, movieOneDescription, movieOneDuration, adventureCategoryName);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.addMovie(movieOneTitle, "Different description", 100, comedyCategoryName);
        });
        assertTrue(exception.getMessage().contains("Movie with title"));
    }

    @Test
    @DisplayName("Get movie by ID successfully")
    void getMovieByIdSuccessfully() {
        Movie addedMovie = movieService.addMovie(movieOneTitle, movieOneDescription, movieOneDuration, adventureCategoryName);
        Movie foundMovie = movieService.getMovieById(addedMovie.getId());
        assertNotNull(foundMovie);
        assertEquals(addedMovie.getId(), foundMovie.getId());
    }

    @Test
    @DisplayName("Get movie by ID returns null for non-existent ID")
    void getMovieByIdReturnsNullForNonExistentId() {
        Movie foundMovie = movieService.getMovieById("nonExistentId");
        assertNull(foundMovie);
    }

    @Test
    @DisplayName("Get all movies returns list of movies")
    void getAllMoviesReturnsListOfMovies() {
        movieService.addMovie(movieOneTitle, movieOneDescription, movieOneDuration, adventureCategoryName);
        movieService.addMovie(movieTwoTitle, movieTwoDescription, movieTwoDuration, comedyCategoryName);
        List<Movie> movies = movieService.getAllMovies();
        assertEquals(2, movies.size());
    }

    @Test
    @DisplayName("Search movies by title returns matching movies")
    void searchMoviesByTitleReturnsMatchingMovies() {
        movieService.addMovie("Alpha Movie", "Desc", 100, adventureCategoryName);
        movieService.addMovie("Beta Alpha Film", "Desc", 100, comedyCategoryName);
        movieService.addMovie("Gamma Picture", "Desc", 100, comedyCategoryName);

        List<Movie> foundMovies = movieService.searchMoviesByTitle("Alpha");
        assertEquals(2, foundMovies.size());
    }

    @Test
    @DisplayName("Get movies by category returns correct movies")
    void getMoviesByCategoryReturnsCorrectMovies() {
        movieService.addMovie("Action Movie", "Desc", 120, adventureCategoryName);
        movieService.addMovie("Comedy Movie", "Desc", 90, comedyCategoryName);
        movieService.addMovie("Another Action", "Desc", 110, adventureCategoryName);

        List<Movie> adventureMovies = movieService.getMoviesByCategory(adventureCategoryName);
        assertEquals(2, adventureMovies.size());

        List<Movie> comedyMovies = movieService.getMoviesByCategory(comedyCategoryName);
        assertEquals(1, comedyMovies.size());
    }

    @Test
    @DisplayName("Get movies by category returns empty list for non-existent category")
    void getMoviesByCategoryReturnsEmptyListForNonExistentCategory() {
        List<Movie> movies = movieService.getMoviesByCategory("NonExistentCategory");
        assertTrue(movies.isEmpty());
    }
}

