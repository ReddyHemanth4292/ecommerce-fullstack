package com.example.ecommercefinal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private int id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int quantity;
    private String sku;
}
