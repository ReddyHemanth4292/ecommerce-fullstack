package com.example.ecommercefinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EcommercefinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommercefinalApplication.class, args);
	}

}
