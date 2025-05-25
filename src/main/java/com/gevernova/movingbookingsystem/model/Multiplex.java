package com.gevernova.movingbookingsystem.model;

import com.gevernova.movingbookingsystem.exceptions.ShowLimitExceeded;

import java.util.*;

public class Multiplex {
    List<Show> shows;

    private final int maxShowSize=25;
    private final String multiplexName;
    Map<String,Movie> showMap;

    public Multiplex(String multiplexName) {
        this.multiplexName = multiplexName;
        shows=new ArrayList<>(25);
        showMap= new HashMap<>();
    }

    public void addShow(Show show,Movie movie) throws ShowLimitExceeded {
        if(shows.size()<maxShowSize){
                shows.add(show);
                showMap.put(show.getShowId(),movie);
        }else{
            throw new ShowLimitExceeded("Maximum number of shows reached");
        }
    }

    public void removeShow(String showId){
        shows.removeIf(show -> show.getShowId().equals(showId));
        showMap.remove(showId);
    }

    public boolean isShowPresent(String showId){
        return showMap.containsKey(showId);
    }

    public Movie getMovie(String showId){
        return showMap.get(showId);
    }
    public List<Show> getShows() {
        return shows;
    }
    public String getMultiplexName() {
        return multiplexName;
    }

    public Optional<Show> getShowById(String showId) {
        return shows.stream().filter(show -> show.getShowId().equals(showId)).findFirst();
    }
}

