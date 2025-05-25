package com.gevernova.onlinecourses.services;

import com.gevernova.onlinecourses.model.Certificate;
import com.gevernova.onlinecourses.model.Course;
import com.gevernova.onlinecourses.model.Module;
import com.gevernova.onlinecourses.model.Student;

import java.util.*;

public class CourseManager {
    private final Map<String, List<CourseProgress>> studentCourseMap = new HashMap<>();

    public void assignCourse(Student student, Course course) {
        studentCourseMap
                .computeIfAbsent(student.getStudentId(), k -> new ArrayList<>())
                .add(new CourseProgress(course));
    }

    public List<CourseProgress> getProgress(Student student) {
        return studentCourseMap.getOrDefault(student.getStudentId(), Collections.emptyList());
    }

    public void updateScore(Student student, Module module, double score) {
        List<CourseProgress> progressList = studentCourseMap.get(student.getStudentId());
        if (progressList == null) return;

        for (CourseProgress cp : progressList) {
            boolean moduleFound = cp.getCourse().getModules()
                    .stream()
                    .anyMatch(m -> m.getModuleId().equals(module.getModuleId()));

            if (!moduleFound) {
                throw new IllegalArgumentException("Module " + module.getName() + " is not part of the course " + cp.getCourse());
            }

            cp.updateScore(module, score);
        }
    }


    public void awardCertificates(Student student) {
        getProgress(student).stream()
                .filter(CourseProgress::isEligibleForCertificate)
                .forEach(progress -> new Certificate(student, progress.getCourse()).printCertificate());
    }
}

