package com.nothing.stella.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nothing.stella.entity.Address;
import com.nothing.stella.exception.AddressNotFoundException;
import com.nothing.stella.exception.InvalidAddressException;
import com.nothing.stella.exception.UnAuthorizedUserException;
import com.nothing.stella.model.AddressViewModel;
import com.nothing.stella.model.AddressSaveModel;
import com.nothing.stella.repository.AddressRepository;

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
    public List<AddressViewModel> save(String reference, AddressSaveModel addressSaveModel) {
        if (!verify(addressSaveModel)) {
            throw new InvalidAddressException("Error: Address is not valid");
        }
        // fetch userId
        int userId = userService.findUserIdByReference(reference);

        List<Address> addresses = getAddresses(userId);

        // removing main from other addresses
        if (addressSaveModel.isMain()) {
            for (Address a : addresses) {
                if (a.isMain()) {
                    a.setMain(0);
                    addressRepository.save(a);
                }
            }
        }

        Address address = new Address(userId, addressSaveModel);
        addressRepository.save(address);

        return getAddressModels(userId);
    }

    @Override
    public List<AddressViewModel> save(int userId, AddressSaveModel addressSaveModel) {
        if (!verify(addressSaveModel)) {
            throw new InvalidAddressException("Error: Address is not valid");
        }
        List<Address> addresses = getAddresses(userId);

        // removing main from other addresses
        if (addressSaveModel.isMain()) {
            for (Address a : addresses) {
                if (a.isMain()) {
                    a.setMain(0);
                    addressRepository.save(a);
                }
            }
        }

        Address address = new Address(userId, addressSaveModel);
        addressRepository.save(address);

        return getAddressModels(userId);
    }

    @Override
    public List<AddressViewModel> convertToAddressModels(List<Address> addresses) {
        List<AddressViewModel> addressModels = new ArrayList<AddressViewModel>();
        for (Address address : addresses) {
            addressModels.add(new AddressViewModel(address));
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
    public List<AddressViewModel> getAddressModels(int userId) {
        return convertToAddressModels(getAddresses(userId));
    }

    @Override
    public List<AddressViewModel> getAddressModels(String reference) {
        return convertToAddressModels(getAddresses(reference));
    }

    @Override
    public List<AddressViewModel> update(int userId, AddressViewModel updateRequest) {
        int id = updateRequest.getId();
        Optional<Address> optionalAddress = addressRepository.findById(id);

        // verify address exists and fetch Address
        if (!optionalAddress.isPresent()) {
            throw new AddressNotFoundException("Error: invalid address id " + id);
        }
        Address address = optionalAddress.get();

        // verify user is allowed to access the address
        if (address.getUserId() != userId) {
            throw new UnAuthorizedUserException("Error: access denied. you are not allowed to access this address");
        }
        // verify updates
        if (!updateRequest.getStreetAddress().isEmpty()) {
            address.setStreetAddress(updateRequest.getStreetAddress());
        }
        if (!updateRequest.getCity().isEmpty()) {
            address.setCity(updateRequest.getCity());
        }
        if (!updateRequest.getState().isEmpty()) {
            address.setState(updateRequest.getState());
        }
        if (!updateRequest.getPostalCode().isEmpty()) {
            address.setPostalCode(updateRequest.getPostalCode());
        }
        if (!updateRequest.getCountry().isEmpty()) {
            address.setCountry(updateRequest.getCountry());
        }
        if (updateRequest.isMain()) {
            List<Address> addresses = getAddresses(userId);
            for (Address a : addresses) {
                if (a.isMain()) {
                    a.setMain(0);
                    save(a);
                }
            }
            address.setMain(1);
        }
        // save updated address
        save(address);
        return getAddressModels(userId);
    };

    @Override
    public List<AddressViewModel> update(String reference, AddressViewModel updateRequest) {
        int userId = userService.findUserIdByReference(reference);

        int id = updateRequest.getId();
        Optional<Address> optionalAddress = addressRepository.findById(id);
        // verify address exists and fetch Address
        if (!optionalAddress.isPresent()) {
            throw new AddressNotFoundException("Error: invalid address id " + id);
        }
        Address address = optionalAddress.get();
        // verify user is allowed to access the address
        if (address.getUserId() != userId) {
            throw new UnAuthorizedUserException("Error: access denied. you are not allowed to access this address");
        }

        // verify updates
        if (!updateRequest.getStreetAddress().isEmpty()) {
            address.setStreetAddress(updateRequest.getStreetAddress());
        }
        if (!updateRequest.getCity().isEmpty()) {
            address.setCity(updateRequest.getCity());
        }
        if (!updateRequest.getState().isEmpty()) {
            address.setState(updateRequest.getState());
        }
        if (!updateRequest.getPostalCode().isEmpty()) {
            address.setPostalCode(updateRequest.getPostalCode());
        }
        if (!updateRequest.getCountry().isEmpty()) {
            address.setCountry(updateRequest.getCountry());
        }
        if (updateRequest.isMain()) {
            List<Address> addresses = getAddresses(userId);
            for (Address a : addresses) {
                if (a.isMain()) {
                    a.setMain(0);
                    save(a);
                }
            }
            address.setMain(1);
        }
        // save updated address
        save(address);
        return getAddressModels(userId);
    }

    @Override
    public List<AddressViewModel> delete(String reference, int addressId) {
        int userId = userService.findUserIdByReference(reference);

        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        // verify address exists and fetch Address
        if (!optionalAddress.isPresent()) {
            throw new AddressNotFoundException("Error: invalid address id " + addressId);
        }
        Address address = optionalAddress.get();
        // verify user is allowed to access the address
        if (address.getUserId() != userId) {
            throw new UnAuthorizedUserException("Error: access denied. you are not allowed to access this address");
        }

        delete(address);
        // change main address
        if (address.isMain()) {
            List<Address> addresses = getAddresses(userId);
            if (addresses.size() > 0) {
                Address addr = addresses.get(0);
                addr.setMain(1);
                save(addr);
            }
        }
        return getAddressModels(userId);
    }

    @Override
    public List<AddressViewModel> delete(int userId, int addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        // verify address exists and fetch Address
        if (!optionalAddress.isPresent()) {
            throw new AddressNotFoundException("Error: invalid address id " + addressId);
        }
        Address address = optionalAddress.get();
        // verify user is allowed to access the address
        if (address.getUserId() != userId) {
            throw new UnAuthorizedUserException("Error: access denied. you are not allowed to access this address");
        }

        delete(address);
        // change main address
        if (address.isMain()) {
            List<Address> addresses = getAddresses(userId);
            if (addresses.size() > 0) {
                Address addr = addresses.get(0);
                addr.setMain(1);
                save(addr);
            }
        }
        return getAddressModels(userId);
    }

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------

    @Override
    public Address findById(int id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void delete(Address address) {
        addressRepository.delete(address);
    }

    @Override
    public Boolean verify(AddressSaveModel addressModel) {
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
