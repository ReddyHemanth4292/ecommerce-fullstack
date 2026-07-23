package com.example.ecommercefinal.dto;

import com.example.ecommercefinal.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Integer productId;
    private String productName;
    private double price;
    private Integer quantity;
    private Double subtotal;
}
