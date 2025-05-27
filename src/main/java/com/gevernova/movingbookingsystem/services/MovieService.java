package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.Category;
import com.gevernova.movingbookingsystem.model.IDGenerator;
import com.gevernova.movingbookingsystem.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieService {
    private final CategoryService categoryService;
    private final Map<String, Movie> movies;

    public MovieService(CategoryService categoryService) {
        this.categoryService = categoryService;
        this.movies = new java.util.HashMap<>();
    }

    public Movie addMovie(String title, String description, int durationMinutes, String categoryName) {
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            category= categoryService.addCategory(categoryName);
            // Add category automatically
        }
        String id = IDGenerator.generateUniqueId("MOV");
        Movie newMovie = new Movie(id, title, description, durationMinutes, category);
        movies.put(id, newMovie);
        System.out.println("Movie added: " + title);
        return newMovie;
    }

    public Movie getMovieById(String movieId) {
        return movies.get(movieId);
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movies.values().stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByCategory(String categoryName) {
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            return List.of(); // Return empty list if category not found
        }
        return movies.values().stream()
                .filter(movie -> movie.getCategory() != null && movie.getCategory().getId().equals(category.getId()))
                .collect(Collectors.toList());
    }

    public void updateMovie(Movie movie) {
        if (movies.containsKey(movie.getId())) {
            movies.put(movie.getId(), movie);
            System.out.println("Movie updated: " + movie.getTitle());
        } else {
            throw new IllegalArgumentException("Movie not found for update.");
        }
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }

    public boolean isMoviePresent(Movie movie) {
        return movies.containsKey(movie.getId());
    }
}
