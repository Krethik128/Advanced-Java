package com.gevernova.movingbookingsystem.services;


import com.gevernova.movingbookingsystem.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowSchedulingService {
    private Map<String, Show> shows; // Show ID -> Show
    private CategoryService movieCatalogService; // Dependency
    private MovieService movieService; // This was being re-initialized
    private MultiplexManagementService multiplexManagementService; // Dependency

    public ShowSchedulingService(MovieService movieService, MultiplexManagementService multiplexManagementService) {
        this.shows = new HashMap<>();
        this.movieService = movieService; // Use the injected MovieService instance
        this.multiplexManagementService = multiplexManagementService;
    }

    public Show scheduleShow(String movieId, String multiplexId, String screenId, Date showTime) {
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " not found.");
        }

        Multiplex multiplex = multiplexManagementService.getMultiplexById(multiplexId);
        if (multiplex == null) {
            throw new IllegalArgumentException("Multiplex with ID " + multiplexId + " not found.");
        }

        Screen screen = multiplex.getScreenById(screenId);
        if (screen == null) {
            throw new IllegalArgumentException("Screen with ID " + screenId + " not found in multiplex " + multiplexId + ".");
        }

        // Basic check for overlapping shows on the same screen (can be more robust)
        boolean overlap = shows.values().stream()
                .filter(s -> s.getScreen().getId().equals(screenId) && s.getMultiplex().getId().equals(multiplexId))
                .anyMatch(s -> {
                    long existingShowStart = s.getShowTime().getTime();
                    long existingShowEnd = existingShowStart + s.getMovie().getDurationMinutes() * 60 * 1000;
                    long newShowStart = showTime.getTime();
                    long newShowEnd = newShowStart + movie.getDurationMinutes() * 60 * 1000;
                    return (newShowStart < existingShowEnd && newShowEnd > existingShowStart);
                });

        if (overlap) {
            throw new IllegalArgumentException("Show overlaps with an existing show on screen  at " + multiplex.getName() + ".");
        }

        String id = IDGenerator.generateUniqueId("SHW");
        Show newShow = new Show(id, movie, multiplex, screen, showTime);
        shows.put(id, newShow);
        System.out.println("Show scheduled: " + movie.getTitle() + " at " + multiplex.getName() + " (" + screen.getName() + ") on " + showTime);
        return newShow;
    }

    public Show getShowById(String showId) {
        return shows.get(showId);
    }

    public List<Show> getShowsByMovieId(String movieId) {
        return shows.values().stream()
                .filter(show -> show.getMovie().getId().equals(movieId))
                .collect(Collectors.toList());
    }

    public List<Show> getShowsByMultiplexId(String multiplexId) {
        return shows.values().stream()
                .filter(show -> show.getMultiplex().getId().equals(multiplexId))
                .collect(Collectors.toList());
    }

    public List<Show> getAllShows() {
        return new java.util.ArrayList<>(shows.values());
    }
}
