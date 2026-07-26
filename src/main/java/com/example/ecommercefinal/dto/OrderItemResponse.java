package com.example.ecommercefinal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String productName;

    private Double price;

    private Integer quantity;

    private Double subtotal;
}
