package com.nothing.stella.repository;

import com.nothing.stella.entity.*;

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

    Optional<User> findByNumber(String number);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNumber(String number);

    @Query("SELECT u.name FROM User u WHERE u.userId = ?1")
    Optional<String> findNameByUserId(int userId);
}