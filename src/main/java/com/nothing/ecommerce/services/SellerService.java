package com.nothing.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.model.SellerInputModel;
import com.nothing.ecommerce.model.SellerUpgradeModel;
import com.nothing.ecommerce.model.SellerViewModel;

public interface SellerService {
    boolean register(SellerInputModel model);

    boolean upgradeToSeller(String reference, SellerUpgradeModel model);

    boolean verifyUpdate(String token);

    void updateStoreName(String reference, String storeName);

    SellerViewModel updateAddress(String reference, String address);

    SellerViewModel addLogo(String reference, MultipartFile logoFile);
}
