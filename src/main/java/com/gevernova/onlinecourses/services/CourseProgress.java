package com.gevernova.onlinecourses.services;

import com.gevernova.onlinecourses.model.Course;
import com.gevernova.onlinecourses.model.Module;

import java.util.HashMap;
import java.util.Map;

public class CourseProgress {
    private final Course course;
    private final Map<com.gevernova.onlinecourses.model.Module, Double> scores = new HashMap<>(); // score for each module

    public CourseProgress(Course course) {
        this.course = course;
    }

    public void updateScore(Module module, double score) {
        scores.put(module, score);
    }

    public double calculateTotalProgress() {
        return scores.entrySet().stream()
                .mapToDouble(e -> (e.getValue() * e.getKey().getWeight()) / 100.0)
                .sum();
    }

    public boolean isEligibleForCertificate() {
        return calculateTotalProgress() >= 70.0; // 70% threshold
    }

    public Course getCourse() { return course; }
}

