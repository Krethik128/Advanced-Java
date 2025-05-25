package com.gevernova.employeemanagment;

import com.gevernova.employeemanagment.exceptions.EmployeeNotFoundException;
import com.gevernova.employeemanagment.model.Department;
import com.gevernova.employeemanagment.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class HRManager {
    private final List<Employee> allEmployees = new ArrayList<>();

    public void addEmployee(Employee emp) {
        allEmployees.add(emp);
    }

    public void removeEmployee(Employee emp) throws EmployeeNotFoundException {
        if(allEmployees.contains(emp)) {
            allEmployees.remove(emp);
        }
        else{
            throw new EmployeeNotFoundException("Employee not found");
        }
    }

    public boolean  isEmployeePresent(Employee employee) throws EmployeeNotFoundException {
        return allEmployees.contains(employee);
    }
    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    public List<Employee> sortBySalary(boolean descending) {
        return allEmployees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary)
                        .reversed().thenComparing(Employee::getName))
                .collect(Collectors.toList());
    }

    public Map<String,List<Employee>> filterByDepartment(List<Employee> allEmployees) {
        return allEmployees.stream()
                .collect(Collectors.groupingBy(employee -> employee.getDepartment().toString()));
    }

    public Map<Department, List<Employee>> groupByDepartment() {

        return allEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    public void reviewPerformance(String employeeId, double score) {
        allEmployees.stream()
                .filter(e -> e.getEmployeeId().equals(employeeId) )
                .findFirst()
                .ifPresent(e -> e.setPerformanceScore(score));
    }

    public Map<Department, Optional<Employee>> getTopEarnersByDepartment() {
        Map<Department, List<Employee>> grouped = groupByDepartment();
        Map<Department, Optional<Employee>> result = new HashMap<>();

        for (Map.Entry<Department, List<Employee>> entry : grouped.entrySet()) {
            Optional<Employee> top = entry.getValue().stream()
                    .max(Comparator.comparingDouble(Employee::getSalary));
            result.put(entry.getKey(), top);
        }

        return result;
    }


    public void updatePerformanceScore(String employeeId, double score) {
        for (Employee emp : allEmployees) {
            if (emp.getEmployeeId().equals(employeeId) ) {
                emp.setPerformanceScore(score);
                break;
            }
        }
    }
}

