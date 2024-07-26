package com.nothing.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.Product;
import com.nothing.ecommerce.model.ProductIdAndNameModel;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Boolean existsByUserIdAndName(int userId, String name);

    Product findByUserIdAndName(int userId, String name);

    List<Product> findByName(String search);

    List<Product> findByNameContaining(String name);

    List<Product> findByUserId(int userId);

    @Query("SELECT p.price FROM Product p WHERE p.id = ?1")
    Optional<Double> findPriceById(int id);

    @Query("SELECT new com.nothing.ecommerce.model.ProductIdAndNameModel(p.id, p.name) FROM Product p WHERE p.userId = ?1")
    List<ProductIdAndNameModel> findProductIdAndNameByUserId(int userId);

    @Query("SELECT p.userId FROM Product p WHERE p.id = ?1")
    Optional<Integer> findUserIdByProductId(int productId);
}
