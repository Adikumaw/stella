package com.nothing.ecommerce.repository;

import com.nothing.ecommerce.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // that's it ... no need to write any code LOL!

    int findUserIdByEmail(String email);

    int findUserIdByNumber(String number);

    User findByNumber(String number);

    User findByEmail(String email);
}