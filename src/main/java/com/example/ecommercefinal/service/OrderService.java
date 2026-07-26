package com.example.ecommercefinal.service;

import com.example.ecommercefinal.dto.OrderResponse;
import com.example.ecommercefinal.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse checkout();
    List<OrderResponse> getMyOrders();
    OrderResponse getOrderById(Integer orderId);
    OrderResponse cancelOrder(Integer orderId);
    List<OrderResponse> getAllOrders();

}
