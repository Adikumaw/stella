package com.nothing.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.AddressId;
import com.nothing.ecommerce.entity.UserAddress;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, AddressId> {

    public List<UserAddress> findByUserId(int userId);

    public UserAddress findByUserIdAndStreetAddress(int userId, String streetAddress);

    @Modifying
    @Query("UPDATE UserAddress ua SET ua.streetAddress = :streetAddress, ua.city = :city, ua.state = :state, ua.postalCode = :postalCode, ua.country = :country, ua.main = :main WHERE ua.streetAddress = :oldStreetAddress AND ua.userId = :userId")
    public void updateUserAddress(@Param("userId") int userId, @Param("streetAddress") String streetAddress,
            @Param("city") String city, @Param("state") String state, @Param("postalCode") String postalCode,
            @Param("country") String country, @Param("main") int main,
            @Param("oldStreetAddress") String oldStreetAddress);
}
