package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.CartResponse;
import com.example.ecommercefinal.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<CartResponse> getAllItems(){
        return new ResponseEntity<>(cartService.getCart(),HttpStatus.OK);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Integer cartItemId){
        cartService.removeCartItem(cartItemId);
        return new ResponseEntity<>("Cart item removed successfully.",HttpStatus.OK);
    }

}
