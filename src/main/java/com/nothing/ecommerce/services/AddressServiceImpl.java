package com.nothing.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.exception.InvalidAddressException;
import com.nothing.ecommerce.model.AddressModel;
import com.nothing.ecommerce.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;

    // ----------------------------------------------------------------
    // Api methods for User Address
    // ----------------------------------------------------------------

    @Override
    public List<AddressModel> save(String reference, AddressModel addressModel) {
        if (!verify(addressModel)) {
            throw new InvalidAddressException("Error: Address is not valid");
        }
        // fetch userId
        int userId = userService.findUserIdByReference(reference);

        List<Address> addresses = getAddresses(userId);

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
        addressRepository.save(address);

        return getAddressModels(userId);
    }

    @Override
    public List<AddressModel> save(int userId, AddressModel addressModel) {
        if (!verify(addressModel)) {
            throw new InvalidAddressException("Error: Address is not valid");
        }
        List<Address> addresses = getAddresses(userId);

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
        addressRepository.save(address);

        return getAddressModels(userId);
    }

    @Override
    public List<AddressModel> convertToAddressModels(List<Address> addresses) {
        List<AddressModel> addressModels = new ArrayList<AddressModel>();
        for (Address address : addresses) {
            addressModels.add(new AddressModel(address));
        }
        return addressModels;
    }

    @Override
    public List<Address> getAddresses(int userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses != null) {
            return addresses;
        }
        return null;
    }

    @Override
    public List<Address> getAddresses(String reference) {
        int userId = userService.findUserIdByReference(reference);
        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses != null) {
            return addresses;
        }
        return null;
    }

    @Override
    public List<AddressModel> getAddressModels(int userId) {
        return convertToAddressModels(getAddresses(userId));
    }

    @Override
    public List<AddressModel> getAddressModels(String reference) {
        return convertToAddressModels(getAddresses(reference));
    }

    @Override
    public List<AddressModel> update(int userId, AddressModel oldAddress, AddressModel newAddress) {
        List<Address> addresses = getAddresses(userId);

        for (Address address : addresses) {
            if (address.equals(oldAddress)) {
                if (!newAddress.getStreetAddress().isEmpty()) {
                    address.setStreetAddress(newAddress.getStreetAddress());
                }
                if (!newAddress.getCity().isEmpty()) {
                    address.setCity(newAddress.getCity());
                }
                if (!newAddress.getState().isEmpty()) {
                    address.setState(newAddress.getState());
                }
                if (!newAddress.getPostalCode().isEmpty()) {
                    address.setPostalCode(newAddress.getPostalCode());
                }
                if (!newAddress.getCountry().isEmpty()) {
                    address.setCountry(newAddress.getCountry());
                }
                if (newAddress.isMain()) {
                    for (Address a : addresses) {
                        if (a.isMain()) {
                            a.setMain(0);
                            save(a);
                        }
                    }
                    address.setMain(1);
                }
                save(address);
                break;
            }
        }
        return getAddressModels(userId);
    };

    @Override
    public List<AddressModel> update(String reference, AddressModel oldAddress, AddressModel newAddress) {
        int userId = userService.findUserIdByReference(reference);

        List<Address> addresses = getAddresses(userId);

        for (Address address : addresses) {
            if (address.equals(oldAddress)) {
                if (!newAddress.getStreetAddress().isEmpty()) {
                    address.setStreetAddress(newAddress.getStreetAddress());
                }
                if (!newAddress.getCity().isEmpty()) {
                    address.setCity(newAddress.getCity());
                }
                if (!newAddress.getState().isEmpty()) {
                    address.setState(newAddress.getState());
                }
                if (!newAddress.getPostalCode().isEmpty()) {
                    address.setPostalCode(newAddress.getPostalCode());
                }
                if (!newAddress.getCountry().isEmpty()) {
                    address.setCountry(newAddress.getCountry());
                }
                if (newAddress.isMain() && !oldAddress.isMain()) {
                    for (Address a : addresses) {
                        if (a.isMain()) {
                            a.setMain(0);
                            save(a);
                        }
                    }
                    address.setMain(1);
                }
                save(address);
                break;
            }
        }
        return getAddressModels(userId);
    }

    @Override
    public List<AddressModel> delete(String reference, AddressModel addressModel) {
        int userId = userService.findUserIdByReference(reference);

        List<Address> addresses = getAddresses(userId);

        for (Address address : addresses) {
            if (address.equals(addressModel)) {
                delete(address);
                // change main address
                if (address.isMain()) {
                    List<Address> addresses_2 = getAddresses(userId);
                    if (addresses_2.size() > 0) {
                        Address addr = addresses_2.get(0);
                        addr.setMain(1);
                        save(addr);
                    }
                }
            }
        }

        return getAddressModels(userId);
    }

    @Override
    public List<AddressModel> delete(int userId, AddressModel addressModel) {

        List<Address> addresses = getAddresses(userId);

        for (Address address : addresses) {
            if (address.equals(addressModel)) {
                delete(address);
                // change main address
                if (address.isMain()) {
                    List<Address> addresses_2 = getAddresses(userId);
                    if (addresses_2.size() > 0) {
                        Address addr = addresses_2.get(0);
                        addr.setMain(1);
                        save(addr);
                    }
                }
            }
        }

        return getAddressModels(userId);
    }

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void delete(Address address) {
        addressRepository.delete(address);
    }

    @Override
    public Boolean verify(AddressModel addressModel) {
        if (addressModel.getStreetAddress() == null || addressModel.getStreetAddress().isEmpty()) {
            return false;
        } else if (addressModel.getCity() == null || addressModel.getCity().isEmpty()) {
            return false;
        } else if (addressModel.getState() == null || addressModel.getState().isEmpty()) {
            return false;
        } else if (addressModel.getPostalCode() == null || addressModel.getPostalCode().isEmpty()) {
            return false;
        } else if (addressModel.getCountry() == null || addressModel.getCountry().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
