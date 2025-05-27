package onlinebooksystemTests;

import com.gevernova.onlinebooksystem.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BookstoreTest {

    private Bookstore bookstore;
    private Book book1, book2, book3;

    @BeforeEach
    public void setUp() {
        bookstore = new Bookstore();
        book1 = new Book("The Science of Interstellar", new Author("Kip Thorne"),500, Category.SCIENCE);
        book2 = new Book("A Brief History of Time", new Author("Stephen Hawking"),200, Category.SCIENCE);
        book3 = new Book("The Hobbit", new Author("J.R.R. Tolkien"),1500, Category.FICTION);

        bookstore.addBook(book1);
        bookstore.addBook(book2);
        bookstore.addBook(book3);
    }

    @Test
    public void testAddBook() {
        Book newBook = new Book("New Book", new Author("New Author"),600, Category.HISTORY);
        bookstore.addBook(newBook);

        assertTrue(bookstore.getCatalog().contains(newBook));
        assertEquals(4, bookstore.getCatalog().size());
    }

    @Test
    public void testSearchByTitleExactMatch() {
        List<Book> result = bookstore.searchByTitle("The Hobbit");
        assertEquals(1, result.size());
        assertEquals("The Hobbit", result.get(0).getTitle());
    }

    @Test
    public void testSearchByTitlePartialMatch() {
        List<Book> result = bookstore.searchByTitle("history");
        assertEquals(1, result.size());
        assertEquals("A Brief History of Time", result.get(0).getTitle());
    }

    @Test
    public void testSearchByAuthor() {
        List<Book> result = bookstore.searchByAuthor("Kip Thorne");
        assertEquals(1, result.size());
        assertEquals("The Science of Interstellar", result.get(0).getTitle());
    }

    @Test
    public void testSearchByCategory() {
        List<Book> result = bookstore.searchByCategory(Category.SCIENCE);
        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }

    @Test
    public void testSearchByAuthorCaseInsensitive() {
        List<Book> result = bookstore.searchByAuthor("j.r.r. tolkien");
        assertEquals(1, result.size());
        assertEquals("The Hobbit", result.get(0).getTitle());
    }

    @Test
    public void testSearchByTitleNoMatch() {
        List<Book> result = bookstore.searchByTitle("Unknown");
        assertTrue(result.isEmpty());
    }
}

