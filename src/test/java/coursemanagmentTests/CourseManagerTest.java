package coursemanagmentTests;

import static org.junit.jupiter.api.Assertions.*;

import com.gevernova.onlinecourses.model.Course;
import com.gevernova.onlinecourses.model.Module;
import com.gevernova.onlinecourses.model.Student;
import com.gevernova.onlinecourses.services.CourseManager;
import com.gevernova.onlinecourses.services.CourseProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class CourseManagerTest {
    Module moduleOne;
    Module moduleTwo;
    Course courseOne;
    Student studentOne;
    CourseManager courseManagerOne;
    CourseManager courseManagerTwo;

    @BeforeEach
    void setUp() {
        moduleOne = new Module("M1", "Java Basics", 50);
        moduleTwo = new Module("M2", "OOP", 50);
        courseOne = new Course("C101", "Java", Arrays.asList(moduleOne, moduleTwo));
        studentOne = new Student("S1", "Alice");
        courseManagerOne = new CourseManager();
        courseManagerOne.assignCourse(studentOne, courseOne); // fixed line
    }



    @Test
    @DisplayName(" Eligible when all module scores are 100")
    void testFullEligibility() {
        courseManagerOne.updateScore(studentOne, moduleOne, 100);
        courseManagerOne.updateScore(studentOne, moduleTwo, 100);

        CourseProgress progress = courseManagerOne.getProgress(studentOne).get(0);

        assertEquals(100.0, progress.calculateTotalProgress());
        assertTrue(progress.isEligibleForCertificate());
    }


    @Test
    @DisplayName(" Eligible when average score is exactly 70")
    void testEdgeCaseEligibility() {
        courseManagerOne.updateScore(studentOne, moduleOne, 60);
        courseManagerOne.updateScore(studentOne, moduleTwo, 80);

        CourseProgress progress = courseManagerOne.getProgress(studentOne).get(0);

        assertEquals(70.0, progress.calculateTotalProgress());
        assertTrue(progress.isEligibleForCertificate());
    }

    @Test
    @DisplayName(" Not eligible when average score is below 70")
    void testIneligibilityBelowThreshold() {
        courseManagerOne.updateScore(studentOne, moduleOne, 50);
        courseManagerOne.updateScore(studentOne, moduleTwo, 60);

        CourseProgress progress = courseManagerOne.getProgress(studentOne).get(0);

        assertEquals(55.0, progress.calculateTotalProgress());
        assertFalse(progress.isEligibleForCertificate());
    }

    @Test
    @DisplayName(" Not eligible when score for one module is missing")
    void testIncompleteScore() {
        courseManagerOne.updateScore(studentOne, moduleOne, 90);
        // No score for moduleTwo

        CourseProgress progress = courseManagerOne.getProgress(studentOne).get(0);

        assertTrue(progress.calculateTotalProgress() < 70.0);
        assertFalse(progress.isEligibleForCertificate());
    }

    @Test
    @DisplayName("Should throw exception if module is not part of course")
    void testModuleNotInCourse() {
        Module fakeModule = new Module("M3", "Fake", 50);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            courseManagerOne.updateScore(studentOne, fakeModule, 80);
        });

        assertTrue(exception.getMessage().contains("not part of the course"));
    }

    @Test
    @DisplayName(" Score should update if student score is modified")
    void testScoreOverwrite() {
        courseManagerOne.updateScore(studentOne, moduleOne, 40);
        courseManagerOne.updateScore(studentOne, moduleOne, 90); // overwrite
        courseManagerOne.updateScore(studentOne, moduleTwo, 70);

        CourseProgress progress = courseManagerOne.getProgress(studentOne).get(0);

        assertEquals(80.0, progress.calculateTotalProgress()); // (90+70)/2
        assertTrue(progress.isEligibleForCertificate());
    }




}

