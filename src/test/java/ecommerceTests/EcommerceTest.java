package ecommerceTests;

import com.gevernova.ecommerce.exceptions.*;
import com.gevernova.ecommerce.model.*;
import com.gevernova.ecommerce.services.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class EcommerceTest {
    ProductInventory productInventory;
    Product mobilePhone, laptop, clothes, other;
    List<Product> products;
    CustomerOrder customerOrder;
    SmsNotificationService smsNotification;
    EmailNotificationService emailNotification;
    PlaceOrder placeOrder;

    @BeforeEach
    public void setup() {
        mobilePhone = new Product(ProductCategory.ELECTRONICS, "MobilePhone", 100.0, 10);
        laptop = new Product(ProductCategory.ELECTRONICS, "Laptop", 200.0, 2);
        clothes = new Product(ProductCategory.CLOTHING, "Shirt", 10.0, 10);
        other = new Product(ProductCategory.OTHERS, "chocolate", 100.0, 10);
        products = new ArrayList<>(List.of(mobilePhone, laptop, clothes, other));
        productInventory = new ProductInventory(products);

        productInventory.addProduct(mobilePhone);
        productInventory.addProduct(laptop);
        productInventory.addProduct(clothes);
        productInventory.addProduct(other);

        smsNotification = new SmsNotificationService();
        emailNotification = new EmailNotificationService();
        placeOrder = new PlaceOrder(List.of(emailNotification, smsNotification));

        customerOrder = new CustomerOrder("Krethik,",1,"krethik3114@gmail.com","784427587593",productInventory,placeOrder);
    }

    // Product Tests
    @DisplayName("Testing valid product creation...")
    @Test
    public void validProductCreation() {
        Product newProduct = new Product(ProductCategory.ELECTRONICS, "Television", 500.0, 5);
        assertNotNull(newProduct.getId());
        assertEquals("Television", newProduct.getName());
        assertEquals(500.0, newProduct.getPrice());
        assertEquals(5, newProduct.getQuantity());
    }

    @DisplayName("Testing product creation with negative price...")
    @Test
    public void invalidProductNegativePrice() {
        assertThrows(InvalidProductException.class, () -> new Product(ProductCategory.ELECTRONICS, "Headphones", -10.0, 10));
    }

    @DisplayName("Testing product creation with empty name...")
    @Test
    public void invalidProductEmptyName() {
        assertThrows(InvalidProductException.class, () -> new Product(ProductCategory.ELECTRONICS, "", 100.0, 10));
    }

    @DisplayName("Testing if existing product is found...")
    @Test
    public void isProductPresentPositive() {
        assertTrue(productInventory.isProductPresent(mobilePhone));
    }

    @DisplayName("Testing if non-existent product is not found...")
    @Test
    public void isProductPresentNegative() {
        Product fake = new Product(ProductCategory.BOOKS, "GhostBook", 20.0, 1);
        assertFalse(productInventory.isProductPresent(fake));
    }

    @DisplayName("Testing roduct quantity fetch...")
    @Test
    public void getProductQuantityPositive() {
        assertEquals(10, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing add new product to inventory...")
    @Test
    public void addProductNewProduct() {
        Product newBook = new Product(ProductCategory.BOOKS, "Novel", 20.0, 5);
        productInventory.addProduct(newBook, 5);
        assertTrue(productInventory.isProductPresent(newBook));
        assertEquals(5, productInventory.getProductQuantity(newBook));
    }

    @DisplayName("Testing quantity update for existing product...")
    @Test
    public void addProductExistingProductUpdatesQuantity() {
        productInventory.addProduct(mobilePhone, 5);
        assertEquals(15, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing quantity addition via updateProduct...")
    @Test
    public void updateProductExistingProduct() {
        productInventory.updateProduct(mobilePhone, 5);
        assertEquals(10, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing update for non-existent product...")
    @Test
    public void updateProductNonExistentProduct() {
        Product ghost = new Product(ProductCategory.BOOKS, "GhostBook", 20.0, 1);
        assertThrows(ProductNotFoundException.class, () -> productInventory.updateProduct(ghost, 5));
    }

    @DisplayName("Testing deletion of existing product...")
    @Test
    public void deleteProductExistingProduct() {
        productInventory.deleteProduct(clothes);
        assertFalse(productInventory.isProductPresent(clothes));
    }

    @DisplayName("Testing deletion of non-existent product...")
    @Test
    public void deleteProductNonExistentProduct() {
        Product ghost = new Product(ProductCategory.BOOKS, "GhostBook", 20.0, 1);
        assertThrows(ProductNotFoundException.class, () -> productInventory.deleteProduct(ghost));
    }

    @DisplayName("Testing partial quantity removal...")
    @Test
    public void removeProductQuantityPartial() {
        productInventory.removeProductQuantity(mobilePhone, 5);
        assertEquals(5, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing excessive quantity removal...")
    @Test
    public void removeProductQuantityExceeds() {
        assertThrows(ProductQuantityExceededException.class, () -> productInventory.removeProductQuantity(mobilePhone, 15));
    }

    @DisplayName("Testing product addition to order within inventory limit...")
    @Test
    public void addProductToOrderAvailableQuantity() {
        customerOrder.addProduct(mobilePhone, 3);
        assertEquals(3, customerOrder.order.get(mobilePhone));
        assertEquals(7, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing product addition to order with insufficient inventory...")
    @Test
    public void addProductToOrderMoreThanAvailable() {
        customerOrder.addProduct(laptop, 5);
        assertEquals(2, customerOrder.order.get(laptop));
        assertFalse(productInventory.isProductPresent(laptop));
    }

    @DisplayName("Testing order update to higher quantity with sufficient inventory...")
    @Test
    public void updateProductInOrderIncreaseQuantitySufficientInventory() {
        customerOrder.addProduct(mobilePhone, 2);
        customerOrder.updateProduct(mobilePhone, 5);
        assertEquals(7, customerOrder.order.get(mobilePhone));
        assertEquals(3, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing order update to lower quantity...")
    @Test
    public void updateProductInOrderDecreaseQuantity() {
        customerOrder.addProduct(mobilePhone, 5);
        customerOrder.updateProduct(mobilePhone, 2);
        assertEquals(7, customerOrder.order.get(mobilePhone));
        assertEquals(3, productInventory.getProductQuantity(mobilePhone));
    }

    @DisplayName("Testing deletion of product from order...")
    @Test
    public void deleteProductFromOrderExistingProduct() {
        customerOrder.addProduct(clothes, 3);
        customerOrder.deleteProduct(clothes);
        assertFalse(customerOrder.order.containsKey(clothes));
        assertEquals(10, productInventory.getProductQuantity(clothes));
    }

    @DisplayName("Testing order placement when order is empty...")
    @Test
    public void placeOrderEmptyOrder() {
        customerOrder.placeOrder();
        assertEquals(0, customerOrder.getOrderSize());
    }

    @DisplayName("Testing successful order placement..")
    @Test
    public void placeOrder() {
        customerOrder.addProduct(mobilePhone, 20);
        customerOrder.addProduct(clothes, 1);
        customerOrder.addProduct(other, 2);
        customerOrder.placeOrder();

        boolean success = placeOrder.placeOrder(customerOrder);
        assertTrue(success);
    }

    @DisplayName("Testing Unsuccessful order placement..")
    @Test
    public void placeOrderUnsuccessful() {
        boolean successStatus=placeOrder.placeOrder(customerOrder);
        assertFalse(successStatus);
    }
}
