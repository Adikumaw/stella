package com.nothing.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nothing.ecommerce.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    VerificationToken findByToken(String token);

    VerificationToken findByUserId(int UserId);
}
