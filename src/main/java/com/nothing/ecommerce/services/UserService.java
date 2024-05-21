package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.model.AddressModel;
import com.nothing.ecommerce.model.UserModel;

public interface UserService {

    // ----------------------------------------------------------------
    // service methods for user
    // ----------------------------------------------------------------
    public boolean registerUser(UserModel userModel);

    public User getUser(int userId);

    public User updateUserName(int userId, String name);

    public User updateUserNumber(int userId, String number);

    public User updateUserEmail(int userId, String email);

    public User updateUserPassword(int userId, String oldPassword, String newPassword);

    public boolean deactivateUser(int userId);

    public void removeUser(int userId);

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    public Address saveAddress(String reference, AddressModel addressModel);

    public List<Address> getUserAddresses(int userId);

    public Address getUserAddress(int userId, String streetAddress);

    public Address updateAddress(String streetAddress,
            String city, String state, String postalCode, String country, int main, String oldStreetAddress,
            int userId);

    public void removeAddress(Address address);
}
