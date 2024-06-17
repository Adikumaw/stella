package com.nothing.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nothing.ecommerce.entity.Roles;
import java.util.List;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    List<Roles> findByUserId(int userId);
}
