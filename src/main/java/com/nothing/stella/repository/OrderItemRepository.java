package com.nothing.stella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nothing.stella.entity.OrderItem;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(int orderId);

    List<OrderItem> findByProductId(int productId);

    @Query("SELECT oi.productId FROM OrderItem oi WHERE oi.orderItemId = ?1")
    Optional<Integer> findProductIdByOrderItemId(int orderItemId);
}
