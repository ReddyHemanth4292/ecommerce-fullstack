package com.example.ecommercefinal.exception;

import com.example.ecommercefinal.controller.ProductController;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String msg){
        super(msg);
    }
}
