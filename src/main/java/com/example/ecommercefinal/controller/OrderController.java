package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.OrderResponse;
import com.example.ecommercefinal.entity.Order;
import com.example.ecommercefinal.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {

        OrderResponse response = orderService.checkout();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(){

        return new ResponseEntity<>(orderService.getMyOrders(),HttpStatus.OK);
    }
}
