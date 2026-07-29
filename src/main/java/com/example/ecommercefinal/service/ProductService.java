package com.example.ecommercefinal.service;

import com.example.ecommercefinal.dto.PageResponse;
import com.example.ecommercefinal.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    PageResponse<Product> getAllProducts(int page, int size,String sortBy, String direction);
    Product getProductById(int id) throws Exception;
    boolean deleteProduct(int id);
    Product updateProduct(int id, Product product);
    List<Product> getProductsByBrand(String brand);
    List<Product> searchProducts(String keyword);
    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);
}
