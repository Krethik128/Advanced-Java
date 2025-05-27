package moviebookingservicesTests;

import com.gevernova.movingbookingsystem.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.gevernova.movingbookingsystem.model.Category;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private String actionCategoryName;
    private String comedyCategoryName;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService();
        actionCategoryName = "Action";
        comedyCategoryName = "Comedy";
    }

    @Test
    @DisplayName("Add a new category successfully")
    void addCategorySuccessfully() {
        Category newCategory = categoryService.addCategory(actionCategoryName);
        assertNotNull(newCategory, "Category should be created");
        assertEquals(actionCategoryName, newCategory.getName(), "Category name should match");
        assertNotNull(newCategory.getId(), "Category ID should be generated");
    }

    @Test
    @DisplayName("Add category fails when name already exists")
    void addCategoryFailsForExistingName() {
        categoryService.addCategory(actionCategoryName); // Add first category
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.addCategory(actionCategoryName);
        });
        assertEquals("Category '" + actionCategoryName + "' already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Get category by ID successfully")
    void getCategoryByIdSuccessfully() {
        Category addedCategory = categoryService.addCategory(actionCategoryName);
        Category foundCategory = categoryService.getCategoryById(addedCategory.getId());
        assertNotNull(foundCategory, "Category should be found by ID");
        assertEquals(addedCategory.getId(), foundCategory.getId(), "Found category ID should match");
    }

    @Test
    @DisplayName("Get category by ID returns null for non-existent ID")
    void getCategoryByIdReturnsNullForNonExistentId() {
        Category foundCategory = categoryService.getCategoryById("nonExistentIdValue");
        assertNull(foundCategory, "Should return null for a non-existent category ID");
    }

    @Test
    @DisplayName("Get category by name successfully")
    void getCategoryByNameSuccessfully() {
        categoryService.addCategory(actionCategoryName);
        Category foundCategory = categoryService.getCategoryByName(actionCategoryName);
        assertNotNull(foundCategory, "Category should be found by name");
        assertEquals(actionCategoryName, foundCategory.getName(), "Found category name should match");
    }

    @Test
    @DisplayName("Get category by name returns null for non-existent name")
    void getCategoryByNameReturnsNullForNonExistentName() {
        Category foundCategory = categoryService.getCategoryByName("NonExistentCategoryName");
        assertNull(foundCategory, "Should return null for a non-existent category name");
    }

    @Test
    @DisplayName("Get all categories when empty")
    void getAllCategoriesWhenEmpty() {
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories, "Categories list should not be null");
        assertTrue(categories.isEmpty(), "Categories list should be empty");
    }

    @Test
    @DisplayName("Get all categories when not empty")
    void getAllCategoriesWhenNotEmpty() {
        categoryService.addCategory(actionCategoryName);
        categoryService.addCategory(comedyCategoryName);
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories, "Categories list should not be null");
        assertEquals(2, categories.size(), "Categories list size should be two"); // Corrected from 1 to 2
    }
}

