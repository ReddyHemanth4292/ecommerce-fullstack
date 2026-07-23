package com.example.ecommercefinal.service;

import com.example.ecommercefinal.dto.CartResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    void addProductToCart(Integer productId);

    CartResponse getCart();;
}
