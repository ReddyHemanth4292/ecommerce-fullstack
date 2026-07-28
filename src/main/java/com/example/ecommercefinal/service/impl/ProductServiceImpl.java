package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.PageResponse;
import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.exception.ProductNotFoundException;
import com.example.ecommercefinal.repository.ProductRepository;
import com.example.ecommercefinal.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PageResponse<Product> getAllProducts(int page, int size,String sortBy, String direction) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }

        if (size > 50) {
            throw new IllegalArgumentException("Maximum page size allowed is 50.");
        }

        List<String> allowedSortFields = List.of("id", "name", "brand", "price", "quantity");
        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        if(!direction.equals("asc") && !direction.equals("desc")){
            throw new IllegalArgumentException("Direction must be 'asc' or 'desc'.");
        }

        Sort sort=Sort.by(direction.equalsIgnoreCase("asc")? Sort.Direction.ASC:Sort.Direction.DESC,sortBy);

        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        return new PageResponse<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() ->
         new ProductNotFoundException("Product with "+ id +" not found"));
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

    @Override
    public Product updateProduct(int id, Product product) {
        Product existingProduct=productRepository.findById(id).orElse(null);
        if(existingProduct !=null){
            existingProduct.setBrand(product.getBrand());
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setSku(product.getSku());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantity(product.getQuantity());
            return productRepository.save(existingProduct);
        }

        return null;
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        if(brand==null && !brand.isBlank()){
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        return productRepository.findByBrand(brand);
    }
}
