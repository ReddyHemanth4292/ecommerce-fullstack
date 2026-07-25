package com.example.ecommercefinal.repository;

import com.example.ecommercefinal.entity.Order;
import com.example.ecommercefinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserOrderByOrderDateDesc(User user);


}
