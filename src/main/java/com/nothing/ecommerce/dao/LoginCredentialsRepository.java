package com.nothing.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.LoginCredentials;

@Repository
public interface LoginCredentialsRepository extends JpaRepository<LoginCredentials, Integer> {

}
