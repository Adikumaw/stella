package com.nothing.stella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.stella.entity.UpdateVerificationToken;

@Repository
public interface UpdateVerificationTokenRepository extends JpaRepository<UpdateVerificationToken, Integer> {
    UpdateVerificationToken findByToken(String token);

    UpdateVerificationToken findByUserId(int UserId);

    UpdateVerificationToken findByData(String data);
}
