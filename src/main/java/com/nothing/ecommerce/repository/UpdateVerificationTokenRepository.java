package com.nothing.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nothing.ecommerce.entity.UpdateVerificationToken;

public interface UpdateVerificationTokenRepository extends JpaRepository<UpdateVerificationToken, Integer> {
    UpdateVerificationToken findByToken(String token);

    UpdateVerificationToken findByUserId(int UserId);

    UpdateVerificationToken findByData(String data);
}
