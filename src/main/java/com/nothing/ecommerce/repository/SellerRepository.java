package com.nothing.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nothing.ecommerce.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {

    Boolean existsByStoreName(String storeName);

    Boolean existsByAddress(String address);
}
