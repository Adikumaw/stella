package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.UserAddress;
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
    public UserAddress saveUserAddress(UserAddress userAddress);

    public List<UserAddress> getUserAddresses(int userId);

    public UserAddress getUserAddress(int userId, String streetAddress);

    public UserAddress updateAddress(String streetAddress,
            String city, String state, String postalCode, String country, int main, String oldStreetAddress,
            int userId);

    public void removeAddress(UserAddress address);
}
