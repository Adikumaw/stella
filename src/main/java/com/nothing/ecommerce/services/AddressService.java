package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.model.AddressViewModel;
import com.nothing.ecommerce.model.AddressSaveModel;

public interface AddressService {
    // ----------------------------------------------------------------
    // RestApi methods for User Address
    // ----------------------------------------------------------------

    List<AddressViewModel> save(int userId, AddressSaveModel saveAddressModel);

    List<AddressViewModel> save(String reference, AddressSaveModel saveAddressModel);

    List<Address> getAddresses(int userId);

    List<Address> getAddresses(String reference);

    List<AddressViewModel> getAddressModels(int userId);

    List<AddressViewModel> getAddressModels(String reference);

    List<AddressViewModel> update(String reference, AddressViewModel updateRequest);

    List<AddressViewModel> update(int userId, AddressViewModel updateRequest);

    List<AddressViewModel> delete(String reference, int addressId);

    List<AddressViewModel> delete(int userId, int addressId);

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    void delete(Address address);

    Address save(Address address);

    List<AddressViewModel> convertToAddressModels(List<Address> addresses);

    Boolean verify(AddressSaveModel addressModel);
}
