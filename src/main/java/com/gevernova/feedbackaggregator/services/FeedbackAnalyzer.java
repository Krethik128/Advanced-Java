package com.gevernova.feedbackaggregator.services;

import com.gevernova.feedbackaggregator.model.Feedback;
import com.gevernova.feedbackaggregator.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class FeedbackAnalyzer {
    private final Map<Product, List<Feedback>> feedbackMap = new HashMap<>();

    public void addFeedback(Product product, Feedback feedback) {
        feedbackMap.computeIfAbsent(product, k -> new ArrayList<>()).add(feedback);
    }

    public double getAverageRating(Product product) {
        List<Feedback> feedbackList = feedbackMap.getOrDefault(product, Collections.emptyList());
        return feedbackList.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    public Map<Product, Double> getAllAverageRatings() {
        return feedbackMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().mapToInt(Feedback::getRating).average().orElse(0.0)
                ));
    }

    public List<Product> getLowRatedProducts(double threshold) {
        return getAllAverageRatings().entrySet().stream()
                .filter(entry -> entry.getValue() < threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

