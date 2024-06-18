package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.model.AddressModel;

public interface AddressService {
    // ----------------------------------------------------------------
    // RestApi methods for User Address
    // ----------------------------------------------------------------

    List<AddressModel> save(int userId, AddressModel addressModel);

    List<AddressModel> save(String reference, AddressModel addressModel);

    List<Address> getAddresses(int userId);

    List<Address> getAddresses(String reference);

    List<AddressModel> getAddressModels(int userId);

    List<AddressModel> getAddressModels(String reference);

    List<AddressModel> update(String reference, AddressModel oldAddress, AddressModel newAddress);

    List<AddressModel> update(int userId, AddressModel oldAddress, AddressModel newAddress);

    List<AddressModel> delete(String reference, AddressModel addressModel);

    List<AddressModel> delete(int userId, AddressModel addressModel);

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    void delete(Address address);

    Address save(Address address);

    List<AddressModel> convertToAddressModels(List<Address> addresses);
}
