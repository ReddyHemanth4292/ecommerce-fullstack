package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.repository.ProductRepository;
import com.example.ecommercefinal.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product not found"));
    }

    @Override
    public boolean deleteProduct(int id) {
        //Product product=productRepository.findById(id).orElse(null);
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        else return false;
    }
}
