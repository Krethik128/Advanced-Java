package com.gevernova.movingbookingsystem.model;

import com.gevernova.movingbookingsystem.exceptions.InvalidMovieDetails;

import static com.gevernova.IDGenerator.generateID;

public class Movie {
    private final String movieName;
    private final int movieDuration;
    private final String movieId;

    public Movie(String movieName, int movieDuration) throws InvalidMovieDetails {
        if(movieName.isEmpty() ){
            throw new InvalidMovieDetails("Movie name cannot be empty");
        }else if(movieDuration<=0){
            throw new InvalidMovieDetails("Move movie details");
        }
        this.movieId = generateID();
        this.movieName = movieName;
        this.movieDuration = movieDuration;
    }
    public String getMovieId() {
        return movieId;
    }
    public String getMovieName() {
        return movieName;
    }
    public int getMovieDuration() {
        return movieDuration;
    }
}
