package com.nothing.ecommerce.services;

import com.nothing.ecommerce.model.SellerInputModel;

public interface SellerService {
    boolean register(SellerInputModel model);

    boolean verifyUpdate(String token);
}
