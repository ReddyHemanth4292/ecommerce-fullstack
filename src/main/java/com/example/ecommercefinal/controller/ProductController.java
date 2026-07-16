package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(Product product){
        productService.createProduct(product);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

}
