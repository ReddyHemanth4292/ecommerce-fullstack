package com.example.ecommercefinal.service;

import com.example.ecommercefinal.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(int id) throws Exception;
    boolean deleteProduct(int id);
    Product updateProduct(int id, Product product);
}
