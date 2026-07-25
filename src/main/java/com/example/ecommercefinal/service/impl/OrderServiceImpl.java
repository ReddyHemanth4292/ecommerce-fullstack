package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.OrderItemResponse;
import com.example.ecommercefinal.dto.OrderResponse;
import com.example.ecommercefinal.entity.*;
import com.example.ecommercefinal.repository.CartItemRepository;
import com.example.ecommercefinal.repository.CartRepository;
import com.example.ecommercefinal.repository.OrderRepository;
import com.example.ecommercefinal.repository.UserRepository;
import com.example.ecommercefinal.service.OrderService;
import com.example.ecommercefinal.service.helper.AuthenticatedUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderService {
    private final AuthenticatedUserService authenticatedUserService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(AuthenticatedUserService authenticatedUserService, CartRepository cartRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.authenticatedUserService = authenticatedUserService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderResponse checkout() {
        User user= authenticatedUserService.getCurrentUser();
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found."));
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }
        Order order=new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        Double totalAmount = 0.0;
        List<CartItem> cartItems=cart.getCartItems();
        List<OrderItem> orderItems= cartItems.stream().map(cartItem -> {
            OrderItem orderItem=new OrderItem();
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setSubtotal(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());
        order.getOrderItems().addAll(orderItems);
        order.setTotalAmount(orderItems.stream().mapToDouble(OrderItem::getSubtotal).sum());
        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();

        OrderResponse response=new OrderResponse();
        response.setId(savedOrder.getId());
        response.setOrderDate(savedOrder.getOrderDate());
        response.setStatus(savedOrder.getStatus());
        response.setTotalAmount(savedOrder.getTotalAmount());

        for(OrderItem orderItem : savedOrder.getOrderItems()){
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductName(orderItem.getProductName());
            itemResponse.setPrice(orderItem.getPrice());
            itemResponse.setQuantity(orderItem.getQuantity());
            itemResponse.setSubtotal(orderItem.getSubtotal());
            response.getItems().add(itemResponse);
        }

        return response;
    }

    private OrderResponse mapToOrderResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        for (OrderItem orderItem : order.getOrderItems()) {

            OrderItemResponse itemResponse = new OrderItemResponse();

            itemResponse.setProductName(orderItem.getProductName());
            itemResponse.setPrice(orderItem.getPrice());
            itemResponse.setQuantity(orderItem.getQuantity());
            itemResponse.setSubtotal(orderItem.getSubtotal());

            response.getItems().add(itemResponse);
        }

        return response;
    }

    @Override
    public List<OrderResponse> getMyOrders() {
        User user=authenticatedUserService.getCurrentUser();
        List<Order> orders=orderRepository.findByUserOrderByOrderDateDesc(user);
        List<OrderResponse> responses = new ArrayList<>();

        for(Order order : orders){
            responses.add(mapToOrderResponse(order));
        }

        return responses;
    }
}
