package onlinebooksystemTests;

import com.gevernova.onlinebooksystem.services.DiscountStrategy;
import com.gevernova.onlinebooksystem.services.FlatDiscount;
import com.gevernova.onlinebooksystem.services.NoDiscount;
import com.gevernova.onlinebooksystem.services.PercentageDiscount;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountStrategyTest {

    @Test
    public void testNoDiscount() {
        DiscountStrategy strategy = new NoDiscount();
        assertEquals(300, strategy.applyDiscount(300));
    }

    @Test
    public void testPercentageDiscount() {
        DiscountStrategy strategy = new PercentageDiscount(20);
        assertEquals(240, strategy.applyDiscount(300));
    }

    @Test
    public void testFlatDiscount() {
        DiscountStrategy strategy = new FlatDiscount(100);
        assertEquals(200, strategy.applyDiscount(300));
    }

    @Test
    public void testFlatDiscountExceedsTotal() {
        DiscountStrategy strategy = new FlatDiscount(400);
        assertEquals(0, strategy.applyDiscount(300));
    }
}
