package com.nothing.stella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.stella.entity.Roles;
import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    List<Roles> findByUserId(int userId);
}
