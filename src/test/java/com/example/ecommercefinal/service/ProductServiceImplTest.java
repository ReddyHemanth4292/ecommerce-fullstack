package com.example.ecommercefinal.service;
import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.exception.ProductNotFoundException;
import com.example.ecommercefinal.repository.ProductRepository;

import com.example.ecommercefinal.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldGetProductById() {

        Product product = new Product();

        product.setId(1);
        product.setName("iPhone 16");
        product.setBrand("Apple");
        product.setPrice(80000.0);
        product.setQuantity(10);

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        Product response =
                productService.getProductById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("iPhone 16", response.getName());
        assertEquals("Apple", response.getBrand());
        verify(productRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        when(productRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(999)
        );
    }

}