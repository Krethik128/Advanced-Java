package employeesystemTests;

import com.gevernova.employeemanagment.*;
import com.gevernova.employeemanagment.exceptions.EmployeeNotFoundException;
import com.gevernova.employeemanagment.model.Department;
import com.gevernova.employeemanagment.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HRManagerTest {

    private HRManager manager;

    @BeforeEach
    public void setUp() {
        manager = new HRManager();
        manager.addEmployee(new Employee( "Alice", Department.HR, 50000));
        manager.addEmployee(new Employee( "Bob", Department.ENGINEERING, 80000));
        manager.addEmployee(new Employee( "Charlie", Department.ENGINEERING, 75000));
        manager.addEmployee(new Employee( "David", Department.SALES, 60000));
    }

    @Test
    public void testGroupByDepartment() {
        Map<Department, List<Employee>> grouped = manager.groupByDepartment();
        assertEquals(2, grouped.get(Department.ENGINEERING).size());
        assertEquals(1, grouped.get(Department.SALES).size());
    }

    @Test
    public void testTopEarnersByDepartment() {
        Map<Department, Optional<Employee>> topEarners = manager.getTopEarnersByDepartment();
        assertEquals("Bob", topEarners.get(Department.ENGINEERING).get().getName());
    }

    @Test
    public void testUpdatePerformance() {
        Optional<Employee> sampleEmployee = manager.getAllEmployees().stream().
                findFirst();
        assertTrue(sampleEmployee.isPresent());
        String id = sampleEmployee.get().getEmployeeId();
        manager.updatePerformanceScore(id, 4.5);
        Employee updated = manager.getAllEmployees().stream()
                .filter(emp -> emp.getEmployeeId().equals(id) )
                .findFirst()
                .orElse(null);
        assertNotNull(updated);
        assertEquals(4.5, updated.getPerformanceScore());
    }

    @Test
    public void testSortEmployeesBySalary() {
        List<Employee> sorted = manager.sortBySalary(true);
        assertEquals("Bob", sorted.get(0).getName());
    }

    @Test
    public void groupByDepartment() {
        Map<Department, List<Employee>> grouped = manager.groupByDepartment();
        assertEquals(2, grouped.get(Department.ENGINEERING).size());
        assertEquals(1, grouped.get(Department.SALES).size());

    }


    @Test
    @DisplayName("Should not find employee after removal")
    public void removeEmployee() {
        Employee testEmployee=manager.getAllEmployees().stream().findFirst().get();
        manager.removeEmployee(testEmployee);
        boolean exists = manager.getAllEmployees()
                .stream()
                .anyMatch(emp->emp.equals(testEmployee));
        assertFalse(exists);

    }
    @Test
    @DisplayName("Should throw exception when removing non-existent employee")
    public void testRemoveNonExistentEmployee() {
        // Arrange
        Employee fakeEmployee = new Employee("Ghost", Department.MARKETING, 70000.0);
        // Act & Assert
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> {
            manager.removeEmployee(fakeEmployee);
        });
        assertTrue(exception.getMessage().contains("not found") || exception.getMessage().contains("does not exist"));
    }


}
