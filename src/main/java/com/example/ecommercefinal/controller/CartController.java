package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addProductToCart(@PathVariable Integer productId){
        cartService.addProductToCart(productId);
        return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }
}
