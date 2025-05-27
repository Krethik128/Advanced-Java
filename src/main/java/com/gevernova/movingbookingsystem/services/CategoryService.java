package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.Category;
import com.gevernova.movingbookingsystem.model.IDGenerator;
import com.gevernova.movingbookingsystem.model.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryService {
    private Map<String, Movie> movies; // Movie ID -> Movie
    private Map<String, Category> categories; // Category ID -> Category

    public CategoryService() {
        this.movies = new HashMap<>();
        this.categories = new HashMap<>();
    }

    public Category addCategory(String name) {
        if (categories.values().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Category '" + name + "' already exists.");
        }
        String id = IDGenerator.generateUniqueId("CAT");
        Category newCategory = new Category(id, name);
        categories.put(id, newCategory);
        System.out.println("Category added: " + name);
        return newCategory;
    }

    public Category getCategoryById(String categoryId) {
        return categories.get(categoryId);
    }

    public Category getCategoryByName(String categoryName) {
        return categories.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);
    }
    public List<Category> getAllCategories(){
        return List.copyOf(categories.values());
    }


}