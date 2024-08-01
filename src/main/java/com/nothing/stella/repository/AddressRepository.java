package com.nothing.stella.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.stella.entity.Address;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
        public List<Address> findByUserId(int userId);
}
