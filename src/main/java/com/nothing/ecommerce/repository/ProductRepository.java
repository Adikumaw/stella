package com.nothing.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Boolean existsByUserIdAndName(int userId, String name);

    Product findByUserIdAndName(int userId, String name);

    List<Product> findByName(String search);

    List<Product> findByNameContaining(String name);

    List<Product> findByUserId(int userId);
}
