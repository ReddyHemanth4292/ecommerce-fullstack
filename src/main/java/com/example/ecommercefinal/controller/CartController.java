package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.CartResponse;
import com.example.ecommercefinal.dto.UpdateCartItemRequest;
import com.example.ecommercefinal.service.CartService;
import jakarta.validation.Valid;
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

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Integer cartItemId, @Valid @RequestBody UpdateCartItemRequest request){
        CartResponse response = cartService.updateCartItem(cartItemId,request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping
    public  ResponseEntity<CartResponse> clearCart(){
        cartService.clearCart();
        return new ResponseEntity<>(cartService.getCart(),HttpStatus.OK);
    }

}
