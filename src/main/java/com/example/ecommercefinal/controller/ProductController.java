package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.PageResponse;
import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        Product savedProduct=productService.createProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        return new ResponseEntity<>(productService.getAllProducts(page, size),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        boolean is_deleted= productService.deleteProduct(id);
        if(is_deleted) return new ResponseEntity("Product deleted successfully",HttpStatus.OK);
        else return new ResponseEntity("Product not found",HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody Product product){
        Product updatedProduct=productService.updateProduct(id,product);
        if(updatedProduct!=null){
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        }
        else return new ResponseEntity<>(updatedProduct,HttpStatus.BAD_REQUEST);
    }
}
