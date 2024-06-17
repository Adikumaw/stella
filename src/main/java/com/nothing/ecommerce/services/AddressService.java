package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.model.AddressModel;

public interface AddressService {
    // ----------------------------------------------------------------
    // RestApi methods for User Address
    // ----------------------------------------------------------------
    Address saveAddress(int userId, AddressModel addressModel);

    Address saveAddress(String reference, AddressModel addressModel);

    List<Address> getUserAddresses(int userId);

    List<Address> getUserAddresses(String reference);

    Address getUserAddress(int userId, String streetAddress);

    Address getUserAddress(String reference, String streetAddress);

    Address updateAddress(String streetAddress,
            String city, String state, String postalCode, String country, int main, String oldStreetAddress,
            int userId);

    Address updateAddress(String reference, AddressModel oldAddress, AddressModel newAddress);

    void removeAddress(Address address);

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    List<Address> findByUserId(int userId);

    void delete(Address address);
}
