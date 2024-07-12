package com.nothing.ecommerce.repository;

import com.nothing.ecommerce.entity.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // that's it ... no need to write any code LOL!

    @Query("SELECT u.userId FROM User u WHERE u.email = ?1")
    Optional<Integer> findUserIdByEmail(String email);

    @Query("SELECT u.userId FROM User u WHERE u.number = ?1")
    Optional<Integer> findUserIdByNumber(String number);

    User findByNumber(String number);

    User findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNumber(String number);
}