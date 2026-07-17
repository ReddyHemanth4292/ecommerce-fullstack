package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Product savedProduct=productService.createProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id){
        boolean is_deleted= productService.deleteProduct(id);
        if(is_deleted) return new ResponseEntity(HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product){
        Product updatedProduct=productService.updateProduct(id,product);
        if(updatedProduct!=null){
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        }
        else return new ResponseEntity<>(updatedProduct,HttpStatus.BAD_REQUEST);
    }
}
