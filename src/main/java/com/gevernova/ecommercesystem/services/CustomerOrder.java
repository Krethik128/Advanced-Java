package com.gevernova.ecommercesystem.services;

import com.gevernova.ecommercesystem.exceptions.ProductNotFoundException;
import com.gevernova.ecommercesystem.exceptions.ProductQuantityExceededException;
import com.gevernova.ecommercesystem.model.PlaceOrder;
import com.gevernova.ecommercesystem.model.Product;
import com.gevernova.ecommercesystem.model.Services;

import java.util.HashMap;
import java.util.Map;

public class CustomerOrder implements Services {
    public PlaceOrder placeOrder;
    public Map<Product,Integer> order=new HashMap<>();
    public ProductInventory inventory;
    private final String name;
    private final int orderId;
    private final String email;
    private final String phoneNumber;

    public CustomerOrder(String name, int orderId, String email, String phoneNumber,ProductInventory inventory, PlaceOrder placeOrder) {
        this.name = name;
        this.orderId = orderId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.inventory = inventory;
        this.placeOrder = placeOrder;
    }


    @Override
    public void addProduct(Product product,int requiredQuantity){ //adding product to order
        if(inventory.isProductPresent(product) && inventory.getProductQuantity(product)>=requiredQuantity){
            order.put(product,requiredQuantity);
            inventory.removeProductQuantity(product,requiredQuantity);
        }
        else{
            int presentQuantity=inventory.getProductQuantity(product); //
            if (presentQuantity > 0) {
                order.put(product,presentQuantity);//adding the quantity present in inventory to order
                System.out.println("Required quantity is "+requiredQuantity+" but available quantity is "+presentQuantity+" so adding "+presentQuantity+" of " + product.getName() + " to order.");
                inventory.deleteProduct(product); //deleting product from inventory
            } else {
                System.out.println("Product " + product.getName() + " is out of stock in inventory.");
            }
        }
    }
    @Override
    public void updateProduct(Product product,int requiredQuantity) throws ProductNotFoundException {
        if(order.containsKey(product)){
            if(inventory.getProductQuantity(product)>=requiredQuantity){ //if inventory quantity is greater than required quantity
                order.put(product,order.get(product)+requiredQuantity);
                inventory.removeProductQuantity(product,requiredQuantity);
            }
            else{ //if inventory quantity is less than required quantity
                throw new ProductQuantityExceededException("Product quantity exceeds in inventory");
            }
        }
        else{//if product is not present in order
            throw new ProductNotFoundException("Product not found in Order");
        }
    }
    @Override
    public void deleteProduct(Product product) throws ProductNotFoundException {
        if (order.containsKey(product)) {
            int quantityInOrder = order.get(product);
            order.remove(product);
            inventory.addProduct(product, quantityInOrder); // Return quantity to inventory
            System.out.println("Product " + product.getName() + " removed from order and " + quantityInOrder + " units returned to inventory.");
        } else {
            throw new ProductNotFoundException("Product not found in Order");
        }
    }

    public int getOrderSize(){
        return order.size();
    }

    public void placeOrder(){
        System.out.println("Preparing to place order...");
        if (this.placeOrder != null) {
            this.placeOrder.placeOrder(this); // Pass the current Order instance
        } else {
            System.out.println("Error: PlaceOrder service not configured for this order.");
        }
    }
    public void getOrderDetails(){
        System.out.println("Order Details");
        order.forEach((product,quantity)->{
            System.out.println("Product name "+product.getName()+" with quantity "+quantity);
        });
    }

    public String getName() {
        return name;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
