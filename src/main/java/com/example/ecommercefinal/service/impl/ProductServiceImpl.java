package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.PageResponse;
import com.example.ecommercefinal.dto.ProductResponse;
import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.exception.ProductNotFoundException;
import com.example.ecommercefinal.repository.ProductRepository;
import com.example.ecommercefinal.service.ProductService;
import com.example.ecommercefinal.specification.ProductSpecification;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        logger.info("Creating product with name: {}",product.getName());
        Product saved= productRepository.save(product);
        logger.info("Product created successfully with id: {}", saved.getId());
        return saved;
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

    @Cacheable(value = "products",key="#id")
    @Override
    public ProductResponse getProductById(int id) {
        logger.info("Fetching product {} from database", id);
        Product product=productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with "+ id +" not found"));
        return mapToProductResponse(product);
    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setSku(product.getSku());

        return response;
    }

    @CacheEvict(value = "products", key = "#id")
    @Override
    public boolean deleteProduct(int id) {
        //Product product=productRepository.findById(id).orElse(null);
        if(productRepository.existsById(id)){
            logger.info("Deleting product with id {}", id);
            productRepository.deleteById(id);
            logger.info("Product {} deleted successfully", id);
            return true;
        }

        else {
            logger.warn("Product {} not found for deletion", id);
            return false;
        }
    }
    @CachePut(value = "products", key = "#id")
    @Override
    public Product updateProduct(int id, Product product) {
        Product existingProduct=productRepository.findById(id).orElse(null);
        if(existingProduct !=null){
            logger.info("Updating product with id {}", id);
            existingProduct.setBrand(product.getBrand());
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setSku(product.getSku());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantity(product.getQuantity());
            logger.info("Product {} updated successfully", id);
            return productRepository.save(existingProduct);
        }

        return null;
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        if(brand==null || brand.isBlank()){
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if(keyword==null || keyword.isBlank()){
            throw new IllegalArgumentException(("Search keyword cannot be empty."));
        }
        return productRepository.findByBrandContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Minimum and maximum price are required.");
        }
        if(minPrice<0 || maxPrice<0){
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if(minPrice>maxPrice){
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price.");
        }
        return productRepository.findByPriceBetween(minPrice,maxPrice);
    }

    @Override
    public PageResponse<Product> searchProducts(String brand, String name, Double minPrice, Double maxPrice,int page, int size, String sortBy, String direction) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException(
                    "Minimum price cannot be greater than maximum price."
            );
        }
        logger.debug("Searching products - brand: {}, name: {}, minPrice: {}, maxPrice: {}", brand, name, minPrice, maxPrice);

        Specification<Product> specification = Specification.unrestricted();

        if (brand != null && !brand.isBlank()) {
            specification = specification.and(
                    ProductSpecification.hasBrand(brand));
        }

        if (name != null && !name.isBlank()) {
            specification = specification.and(
                    ProductSpecification.nameContains(name));
        }

        if (minPrice != null) {
            specification = specification.and(
                    ProductSpecification.priceGreaterThanOrEqualTo(minPrice));
        }

        if (maxPrice != null) {
            specification = specification.and(
                    ProductSpecification.priceLessThanOrEqualTo(maxPrice));
        }
        Sort sort=Sort.by(direction.equalsIgnoreCase("asc")? Sort.Direction.ASC:Sort.Direction.DESC,sortBy);
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<Product> productPage=productRepository.findAll(specification,pageable);
        return new PageResponse<>(productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast());

    }
}
