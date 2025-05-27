package com.gevernova.movingbookingsystem.model;

public class Movie {
    private String id;
    private String title;
    private String description;
    private int durationMinutes;
    private Category category; // Association with Category

    public Movie(String id, String title, String description, int durationMinutes, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public Category getCategory() {
        return category.toString() == null ? null : category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", category=" + (category != null ? category.getName() : "N/A") +
                '}';
    }
}