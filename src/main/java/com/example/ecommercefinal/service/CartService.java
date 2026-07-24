package com.example.ecommercefinal.service;

import com.example.ecommercefinal.dto.CartResponse;
import com.example.ecommercefinal.dto.UpdateCartItemRequest;
import org.springframework.http.ResponseEntity;

public interface CartService {
    void addProductToCart(Integer productId);
    CartResponse getCart();
    void removeCartItem(Integer cartItemId);
    CartResponse updateCartItem(Integer cartItemId, UpdateCartItemRequest request);
    void clearCart();
}
