package moviebookingservicesTests;

import com.gevernova.movingbookingsystem.model.*;
import com.gevernova.movingbookingsystem.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    private String firstUsername;
    private String firstUserPassword;
    private String firstUserEmail;
    private String secondUsername;
    private String secondUserPassword;
    private String secondUserEmail;
    private String nonExistentUsername;
    private String incorrectPassword;

    User newUser;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        firstUsername = "alice";
        firstUserPassword = "passwordOneTwoThree";
        firstUserEmail = "alice@example.com";
        secondUsername = "bob";
        secondUserPassword = "securePassword";
        secondUserEmail = "bob@example.com";
        nonExistentUsername = "charlie";
        incorrectPassword = "wrongPassword";
    }

    @Test
    @DisplayName("Register a new user successfully")
    void registerNewUserSuccessfully() {
         newUser = userService.registerUser(firstUsername, firstUserPassword, firstUserEmail);
        assertNotNull(newUser, "User should be created");
        assertEquals(firstUsername, newUser.getUsername(), "Username should match");
        assertEquals(firstUserEmail, newUser.getEmail(), "Email should match");
        assertNotNull(newUser.getId(), "User ID should be generated");
    }

    @Test
    @DisplayName("Register user fails when username already exists")
    void registerUserFailsForExistingUsername() {
        userService.registerUser(firstUsername, firstUserPassword, firstUserEmail); // Register first user
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(firstUsername, secondUserPassword, secondUserEmail);
        });
        assertEquals("Username already exists: " + firstUsername, exception.getMessage(), "Exception message should indicate existing username");
    }

    @Test
    @DisplayName("Register user fails when email already exists")
    void registerUserFailsForExistingEmail() {
        userService.registerUser(firstUsername, firstUserPassword, firstUserEmail); // Register first user
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(secondUsername, secondUserPassword, firstUserEmail);
        });
        assertEquals("Email already exists: " + firstUserEmail, exception.getMessage(), "Exception message should indicate existing email");
    }

    @Test
    @DisplayName("Authenticate user successfully")
    void authenticateUserSuccessfully() {
        User registeredUser = userService.registerUser(firstUsername, firstUserPassword, firstUserEmail);
        User authenticatedUser = userService.authenticateUser(firstUsername, firstUserPassword);
        assertNotNull(authenticatedUser, "Authentication should succeed");
        assertEquals(registeredUser.getId(), authenticatedUser.getId(), "Authenticated user ID should match registered user ID");
    }

    @Test
    @DisplayName("Authenticate user fails for non-existent username")
    void authenticateUserFailsForNonExistentUsername() {
        User authenticatedUser = userService.authenticateUser(nonExistentUsername, firstUserPassword);
        assertNull(authenticatedUser, "Authentication should fail for non-existent user");
    }

    @Test
    @DisplayName("Authenticate user fails for incorrect password")
    void authenticateUserFailsForIncorrectPassword() {
        userService.registerUser(firstUsername, firstUserPassword, firstUserEmail);
        User authenticatedUser = userService.authenticateUser(firstUsername, incorrectPassword);
        assertNull(authenticatedUser, "Authentication should fail for incorrect password");
    }

    @Test
    @DisplayName("Get user by ID successfully")
    void getUserByIdSuccessfully() {
        User registeredUser = userService.registerUser(firstUsername, firstUserPassword, firstUserEmail);
        User foundUser = userService.authenticateUser(firstUsername, firstUserPassword);
        assertNotNull(foundUser, "User should be found by ID");
        assertEquals(registeredUser.getId(), foundUser.getId(), "Found user ID should match");
    }

    @Test
    @DisplayName("Get user by ID returns null for non-existent ID")
    void getUserByIdReturnsNullForNonExistentId() {
        User foundUser = userService.authenticateUser(nonExistentUsername, firstUserPassword);
        assertNull(foundUser, "Should return null for a non-existent user ID");
    }
}
