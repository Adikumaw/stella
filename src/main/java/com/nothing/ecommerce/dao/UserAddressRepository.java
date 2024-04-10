package com.nothing.ecommerce.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.AddressId;
import com.nothing.ecommerce.entity.UserAddress;
import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, AddressId> {

    public List<UserAddress> findByUserId(int userId);
}
