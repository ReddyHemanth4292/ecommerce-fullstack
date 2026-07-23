package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.CartResponse;
import com.example.ecommercefinal.entity.Cart;
import com.example.ecommercefinal.entity.CartItem;
import com.example.ecommercefinal.entity.Product;
import com.example.ecommercefinal.entity.User;
import com.example.ecommercefinal.exception.ProductNotFoundException;
import com.example.ecommercefinal.repository.CartItemRepository;
import com.example.ecommercefinal.repository.CartRepository;
import com.example.ecommercefinal.repository.ProductRepository;
import com.example.ecommercefinal.repository.UserRepository;
import com.example.ecommercefinal.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    @Override
    public void addProductToCart(Integer productId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not Found"));
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product Not Found"));
        Cart cart= cartRepository.findByUser(user).orElse(null);
        if(cart==null){
            cart=new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        CartItem cartItem= cartItemRepository.findByCartAndProduct(cart,product).orElse(null);
        if(cartItem==null){
            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity()+1);
            cartItemRepository.save(cartItem);
        }


    }

    @Override
    public CartResponse getCart() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found"));;
        List<CartItem> cartItems= cartItemRepository.findByCart(cart);
        CartResponse cartResponse=new CartResponse();
        return null;
    }
}
