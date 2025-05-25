package onlinebooksystemTests;

import com.gevernova.onlinebooksystem.exceptions.BookNotFoundException;
import com.gevernova.onlinebooksystem.model.Author;
import com.gevernova.onlinebooksystem.model.Book;
import com.gevernova.onlinebooksystem.model.Cart;
import com.gevernova.onlinebooksystem.services.Category;
import com.gevernova.onlinebooksystem.services.FlatDiscount;
import com.gevernova.onlinebooksystem.services.NoDiscount;
import com.gevernova.onlinebooksystem.services.PercentageDiscount;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    private Cart cart;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setup() {
        cart = new Cart();
        book1 = new Book("Java 101", new Author("John Doe"), 100, Category.SCIENCE);
        book2 = new Book("OOP Design", new Author("Jane Smith"), 150, Category.SCIENCE);
    }

    @Test
    public void testAddAndRemoveBook() {
        cart.addBook(book1);
        cart.addBook(book2);
        assertEquals(2, cart.getItems().size());

        cart.removeBook(book1);
        assertEquals(1, cart.getItems().size());
        assertEquals(book2, cart.getItems().get(0));
    }

    @Test
    public void testCalculateTotalWithNoDiscount() {
        cart.addBook(book1);
        cart.addBook(book2);
        double total = cart.calculateTotal(new NoDiscount());
        assertEquals(250, total);
    }

    @Test
    public void testCalculateTotalWithPercentageDiscount() {
        cart.addBook(book1);
        cart.addBook(book2);
        double total = cart.calculateTotal(new PercentageDiscount(10));
        assertEquals(225, total);
    }

    @Test
    public void testCalculateTotalWithFlatDiscount() {
        cart.addBook(book1);
        cart.addBook(book2);
        double total = cart.calculateTotal(new FlatDiscount(50));
        assertEquals(200, total);
    }

    @Test
    public void removeBookWhichIsNotPresentInCart(){
        Book gameOfThrones=new Book("Ice and fire", new Author("John Doe"), 100, Category.FICTION);
        assertThrows(BookNotFoundException.class,()-> cart.removeBook(gameOfThrones));
    }


}

