package com.gevernova.employeemanagment.model;

import com.gevernova.IDGenerator;
import com.gevernova.employeemanagment.exceptions.EmployeeNotFoundException;

public class Employee {
    private final String employeeId;
    private final String name;
    private final Department department;
    private double salary;

    private double performanceScore;

    public Employee(String name, Department department, double salary){
        this.employeeId = IDGenerator.generateID();
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.performanceScore = 0.0;
    }

    // Getters and Setters

    public Department getDepartment() {
        return department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void setPerformanceScore(double score) {
        this.performanceScore = score;
    }

    public double getPerformanceScore() {
        return performanceScore;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return name + " (" + department + ") - Salary: $" + salary;
    }
}
