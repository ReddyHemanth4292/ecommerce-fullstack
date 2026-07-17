package com.example.ecommercefinal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 25, message = "Product name must be between 3 and 100 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
    @Positive(message = "Price must be greater than zero")
    private double price;
    @PositiveOrZero(message = "Quantity cannot be negative")
    private int quantity;
    @NotBlank(message = "Brand is required")
    private String brand;
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 20, message = "SKU must be between 3 and 20 characters")
    private String sku;

}
