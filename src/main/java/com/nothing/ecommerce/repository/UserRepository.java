package com.nothing.ecommerce.repository;

import com.nothing.ecommerce.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // that's it ... no need to write any code LOL!

    @Query("SELECT u.userId FROM User u WHERE u.email = ?1")
    int findUserIdByEmail(String email);

    @Query("SELECT u.userId FROM User u WHERE u.number = ?1")
    int findUserIdByNumber(String number);

    User findByNumber(String number);

    User findByEmail(String email);
}