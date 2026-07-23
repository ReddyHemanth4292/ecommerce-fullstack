package com.example.ecommercefinal.repository;

import com.example.ecommercefinal.entity.Cart;
import com.example.ecommercefinal.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart>  findByUser(User User);
}
