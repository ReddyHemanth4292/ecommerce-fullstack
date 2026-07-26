package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.OrderResponse;
import com.example.ecommercefinal.dto.UpdateOrderStatusRequest;
import com.example.ecommercefinal.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        List<OrderResponse> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Integer id, @RequestBody UpdateOrderStatusRequest request){
        OrderResponse response = orderService.updateOrderStatus(id,request.getStatus());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
