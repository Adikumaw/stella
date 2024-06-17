package com.nothing.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.exception.AddressNotFoundException;
import com.nothing.ecommerce.model.AddressModel;
import com.nothing.ecommerce.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    @Override
    public Address saveAddress(String reference, AddressModel addressModel) {
        // fetch userId
        int userId = userService.findUserIdByReference(reference);

        List<Address> addresses = getUserAddresses(userId);

        // removing main from other addresses
        if (addressModel.isMain()) {
            for (Address a : addresses) {
                if (a.isMain()) {
                    a.setMain(0);
                    addressRepository.save(a);
                }
            }
        }

        Address address = new Address(userId, addressModel);
        return addressRepository.save(address);
    }

    @Override
    public Address saveAddress(int userId, AddressModel addressModel) {
        List<Address> addresses = getUserAddresses(userId);

        // removing main from other addresses
        if (addressModel.isMain()) {
            for (Address a : addresses) {
                if (a.isMain()) {
                    a.setMain(0);
                    addressRepository.save(a);
                }
            }
        }

        Address address = new Address(userId, addressModel);
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(int userId) {
        List<Address> fetchedUserAddress = addressRepository.findByUserId(userId);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        return null;
    }

    @Override
    public List<Address> getUserAddresses(String reference) {
        int userId = userService.findUserIdByReference(reference);
        List<Address> fetchedUserAddress = addressRepository.findByUserId(userId);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        return null;
    }

    @Override
    public Address getUserAddress(int userId, String streetAddress) {
        Address fetchedUserAddress = addressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        return null;
    }

    @Override
    public Address getUserAddress(String reference, String streetAddress) {
        int userId = userService.findUserIdByReference(reference);

        Address fetchedUserAddress = addressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        return null;
    }

    @Override
    @Transactional
    public Address updateAddress(String streetAddress,
            String city, String state, String postalCode, String country, int main, String oldStreetAddress,
            int userId) {
        Address address = addressRepository.findByUserIdAndStreetAddress(userId, oldStreetAddress);
        if (address != null) {
            addressRepository.updateUserAddress(userId, streetAddress, city, state, postalCode, country, main,
                    oldStreetAddress);

            Address updatedAddress = addressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
            return updatedAddress;
        }
        return null;
    };

    @Override
    @Transactional
    public Address updateAddress(String reference, AddressModel oldAddress, AddressModel newAddress) {
        int userId = userService.findUserIdByReference(reference);

        
        Address address = addressRepository.findByUserIdAndStreetAddress(userId, oldStreetAddress);
        if (address != null) {
            addressRepository.updateUserAddress(userId, streetAddress, city, state, postalCode, country, main,
                    oldStreetAddress);

            Address updatedAddress = addressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
            return updatedAddress;
        }
        return null;
    };

    @Override
    public void removeAddress(Address address) {
        Address userAddress = addressRepository.findByUserIdAndStreetAddress(address.getUserId(),
                address.getStreetAddress());
        if (userAddress != null) {
            addressRepository.delete(userAddress);
            // check the deleted userAddress is main and add main to next userAddress if it
            // exists
            if (userAddress.getMain() == 1) {
                List<Address> addresses = addressRepository.findByUserId(address.getUserId());
                if (addresses != null) {
                    Address newPrimaryAddress = addresses.get(0);
                    newPrimaryAddress.setMain(1);
                    addressRepository.save(newPrimaryAddress);
                }
            }
        } else {
            throw new AddressNotFoundException("Address not found for user " + address.getUserId());
        }
    }

    @Override
    public List<Address> findByUserId(int userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public void delete(Address address) {
        addressRepository.delete(address);
    }
}
