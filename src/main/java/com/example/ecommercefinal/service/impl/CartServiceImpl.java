package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.CartItemResponse;
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
import com.example.ecommercefinal.service.helper.AuthenticatedUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public CartServiceImpl(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, AuthenticatedUserService authenticatedUserService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Transactional
    @Override
    public void addProductToCart(Integer productId) {
        User user = authenticatedUserService.getCurrentUser();
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
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        User user = authenticatedUserService.getCurrentUser();

        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found"));
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemResponse> itemResponses=cartItems.stream().map(cartItem -> {
            Product product= cartItem.getProduct();
            Double subtotal=product.getPrice() * cartItem.getQuantity();
            return new CartItemResponse(product.getId(),product.getName(),product.getPrice(),cartItem.getQuantity(),subtotal);
        }).collect(Collectors.toList());
        Integer totalItems=itemResponses.stream().mapToInt(CartItemResponse::getQuantity).sum();
        Double totalPrice=itemResponses.stream().mapToDouble(CartItemResponse::getSubtotal).sum();
        return new CartResponse(itemResponses,totalItems,totalPrice);
    }

    @Override
    @Transactional
    public void removeCartItem(Integer cartItemId) {
        User user = authenticatedUserService.getCurrentUser();

        Cart cart=cartRepository.findByUser(user).orElseThrow(()-> new RuntimeException("Cart not Found"));
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(()-> new RuntimeException("Cart item not found"));

        if(cartItem.getCart().getId()!=cart.getId()){
            throw new RuntimeException("You are not allowed to delete this cart item.");
        }
        cartItemRepository.delete(cartItem);

    }
}
