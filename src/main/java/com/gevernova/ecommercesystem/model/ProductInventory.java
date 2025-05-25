package com.gevernova.ecommerce.model;

import com.gevernova.ecommerce.exceptions.ProductNotFoundException;
import com.gevernova.ecommerce.exceptions.ProductQuantityExceededException;
import com.gevernova.ecommerce.services.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInventory implements Services {
    List<Product> productList=new ArrayList<>();
    private final Map<String, Integer> productStock =new HashMap<>();

    public ProductInventory() {}

    public ProductInventory(List<Product> products) {
        for(Product product:products){
            this.productStock.put(product.getId(),product.getQuantity());
            productList.add(product);
        }
    }

    public void addProduct(Product product){//
        this.productStock.put(product.getId(),product.getQuantity());
    }

    public void addProduct(Product product,int quantity){
        if(!isProductPresent(product)){
            productList.add(product);
            productStock.put(product.getId(),product.getQuantity());
            System.out.println("Product is added");
        }else {
            int existingQty = productStock.get(product.getId());
            productStock.put(product.getId(), existingQty + quantity);
            System.out.println("Product quantity increased by " + quantity + " in inventory.");
        }
    }

    public void updateProduct(Product product,int quantity) throws ProductNotFoundException {
        if(productStock.containsKey(product.getId())){
            double prize=product.getQuantity()*product.getPrice();
            product.setQuantity(quantity+product.getQuantity());
            product.setPrice(prize/quantity);
            System.out.println("Product is already present quantity updated to "+quantity+" and price updated to "+prize+"");
        }
        else{
            throw new ProductNotFoundException("Product not found");
        }
    }

    public void deleteProduct(Product product) throws ProductNotFoundException {
        if(productStock.containsKey(product.getId())){
            productStock.remove(product.getId());
            productList.remove(product);
            System.out.println("Product is removed from inventory");
        }
        else{
            throw new ProductNotFoundException("Product not found in inventory");
        }
    }

    public void removeProductQuantity(Product product,int removeQuantity) throws ProductNotFoundException, ProductQuantityExceededException {
        if(productStock.containsKey(product.getId())){//if product is present in inventory
            int presentQuantity= productStock.get(product.getId());
            if(presentQuantity>removeQuantity){ //if product quantity is greater than remove quantity
                productStock.put(product.getId(),presentQuantity-removeQuantity);
                System.out.println("Quantity is updated");
            }
            else if(presentQuantity==removeQuantity){//if product quantity is equal to remove quantity
                productList.remove(product);
                ProductInventory inventory=new ProductInventory(); //to use methods of inventory
                inventory.deleteProduct(product);
            }
            else{//if product quantity is less than remove quantity
                throw new ProductQuantityExceededException("Inventory quantity exceeds");
            }
        }
        else{//if a product is not present in inventory
            throw new ProductNotFoundException("Product not found");
        }
    }
    public Boolean isProductPresent(Product product) throws ProductNotFoundException {
        if(productStock.containsKey(product.getId())){
            System.out.println("Product is present  "+productStock.get(product.getId()));
        }
        return productStock.containsKey(product.getId());
    }
    public int getProductQuantity(Product product) throws ProductNotFoundException {
        return productStock.get(product.getId());
    }




    

}
