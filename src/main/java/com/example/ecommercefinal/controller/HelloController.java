package com.example.ecommercefinal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("hello")
    public String hello(){
        return "Good Start!!";
    }

    @GetMapping("/test")
    public String test() {
        int result = 10 / 0;
        return "Success";
    }
}
