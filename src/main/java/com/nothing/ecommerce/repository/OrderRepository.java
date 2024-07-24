package com.nothing.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.Order;
import com.nothing.ecommerce.model.StatusAndDateModel;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByRazorpayId(String razorpayId);

    @Query("SELECT o.status FROM Order o WHERE o.orderId = ?1")
    Optional<String> findStatusByOrderId(int orderId);

    List<Order> findByUserId(int userId);

    @Query("SELECT new com.nothing.ecommerce.model.StatusAndDateModel(o.status, o.orderDate) FROM Order o WHERE o.orderId = ?1 AND o.status != 'created' AND o.status != 'creating'")
    Optional<StatusAndDateModel> findPaidStatusAndOrderDateByOrderId(int orderId);
}
