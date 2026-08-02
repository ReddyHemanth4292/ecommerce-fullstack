package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.config.CacheNames;
import com.example.ecommercefinal.dto.OrderItemResponse;
import com.example.ecommercefinal.dto.OrderResponse;
import com.example.ecommercefinal.entity.*;
import com.example.ecommercefinal.exception.*;
import com.example.ecommercefinal.repository.*;
import com.example.ecommercefinal.service.OrderService;
import com.example.ecommercefinal.service.helper.AuthenticatedUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
    private final ProductRepository productRepository;
    private final CacheManager cacheManager;

    public OrderServiceImpl(AuthenticatedUserService authenticatedUserService, CartRepository cartRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository, ProductRepository productRepository, CacheManager cacheManager) {
        this.authenticatedUserService = authenticatedUserService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cacheManager = cacheManager;
    }
    private static final Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrderResponse checkout() {
        User user= authenticatedUserService.getCurrentUser();
        logger.info("Checkout started for user {}", user.getId());
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new CartNotFoundException("Cart not found."));
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }
        logger.info("Checkout started for user {}", user.getId());
        Order order=new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        List<CartItem> cartItems=cart.getCartItems();
        List<OrderItem> orderItems= new ArrayList<>();
        for(CartItem cartItem:cartItems) {
            Product product = productRepository.findById(
                            cartItem.getProduct().getId())
                    .orElseThrow(() ->
                            new ProductNotFoundException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            if (product.getQuantity() < cartItem.getQuantity()) {
                logger.warn("Insufficient stock for product {}. Requested={}, Available={}",
                        product.getId(), cartItem.getQuantity(), product.getQuantity());
                throw new InsufficientStockException(
                        "Not enough stock for " + product.getName());
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice() * cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            Cache cache = cacheManager.getCache(CacheNames.PRODUCTS);
            if(cache!=null){
                cache.evict(product.getId());
            }
        }
        order.getOrderItems().addAll(orderItems);
        order.setTotalAmount(orderItems.stream().mapToDouble(OrderItem::getSubtotal).sum());
        Order savedOrder = orderRepository.save(order);
        logger.info("Order {} created successfully for user {}", savedOrder.getId(), user.getId());
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        return mapToOrderResponse(savedOrder);
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

    @Override
    public OrderResponse getOrderById(Integer orderId) {
        User user=authenticatedUserService.getCurrentUser();
        //Order order= orderRepository.findByUserAndId(user,orderId).orElseThrow(()->new RuntimeException("order not found exception"));
        //OrderResponse response=mapToOrderResponse(order);
        Order order=orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order Not found"));
        if(!order.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not allowed to access this order.");
        }
        OrderResponse response=mapToOrderResponse(order);
        return response;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Integer orderId) {
        User user=authenticatedUserService.getCurrentUser();
        Order order=orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(""));
        if(!order.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not allowed to access this order.");
        }
        if(order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.PROCESSING){
            throw new InvalidOrderStateException("Only placed or processing orders can be cancelled.");
        }

        List<OrderItem> orderItems=order.getOrderItems();
        for(OrderItem orderItem : orderItems){
            Product product = productRepository.findById(orderItem.getProduct().getId())
                    .orElseThrow(() ->
                            new ProductNotFoundException("Product not found"));
           product.setQuantity(product.getQuantity() + orderItem.getQuantity());
           productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder =orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order>orders=orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PLACED -> next == OrderStatus.PROCESSING || next == OrderStatus.CANCELLED;
            case PROCESSING -> next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order=orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order not found"));
        OrderStatus currentStatus=order.getStatus();
        if(!isValidStatusTransition(currentStatus,status)){
            throw new InvalidOrderStateException("Invalid order status transition from "
                    + currentStatus + " to " + status);
        }
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }
}
