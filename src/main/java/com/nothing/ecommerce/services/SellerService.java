package com.nothing.ecommerce.services;

import com.nothing.ecommerce.model.SellerInputModel;
import com.nothing.ecommerce.model.SellerViewModel;

public interface SellerService {
    boolean register(SellerInputModel model);

    boolean verifyUpdate(String token);

    void updateStoreName(String reference, String storeName);

    SellerViewModel updateAddress(String reference, String address);
}
