package com.nothing.ecommerce.dao;

import com.nothing.ecommerce.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Integer> {
    // that's it ... no need to write any code LOL!
    UserDetails findByNumber(String number);

    UserDetails findByEmail(String email);
}