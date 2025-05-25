package com.gevernova.onlinecourses.model;
public class Certificate {
    private final Student student;
    private final Course course;

    public Certificate(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public void printCertificate() {
        System.out.println("Certificate awarded to: " + student.getName() + " for completing " + course.getTitle());
    }
}

