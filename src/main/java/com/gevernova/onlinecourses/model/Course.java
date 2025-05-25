package com.gevernova.onlinecourses.model;

import java.util.List;

public class Course {
    private final String courseId;
    private final String title;
    private final List<Module> modules;

    public Course(String courseId, String title, List<Module> modules) {
        this.courseId = courseId;
        this.title = title;
        this.modules = modules;
    }

    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public List<Module> getModules() { return modules; }
}

