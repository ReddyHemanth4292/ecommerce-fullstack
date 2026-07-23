package com.example.ecommercefinal.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Good Start!!";
    }

    @GetMapping("/test")
    public String test() {
        int result = 10 / 0;
        return "Success";
    }

    @GetMapping("/passEncode")
    public String passwordEncode(@RequestParam String password){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
