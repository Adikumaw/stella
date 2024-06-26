package com.nothing.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.UpdateVerificationToken;

@Repository
public interface UpdateVerificationTokenRepository extends JpaRepository<UpdateVerificationToken, Integer> {
    UpdateVerificationToken findByToken(String token);

    UpdateVerificationToken findByUserId(int UserId);

    UpdateVerificationToken findByData(String data);
}
