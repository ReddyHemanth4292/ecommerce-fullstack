package com.example.ecommercefinal.service;

import com.example.ecommercefinal.entity.Product;

import java.util.List;

public interface ProductService {
    public Product createProduct(Product product);
    public List<Product> getAllProducts();
    public Product getProductById(int id);
    public boolean deleteProduct(int id);
}
