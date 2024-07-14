package com.nothing.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByRazorpayId(String razorpayId);

    List<Order> findByUserId(int userId);
}
