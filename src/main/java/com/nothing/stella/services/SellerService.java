package com.nothing.stella.services;

import org.springframework.web.multipart.MultipartFile;

import com.nothing.stella.model.SellerInputModel;
import com.nothing.stella.model.SellerUpgradeModel;
import com.nothing.stella.model.SellerViewModel;

public interface SellerService {
    boolean register(SellerInputModel model);

    boolean upgradeToSeller(String reference, SellerUpgradeModel model);

    boolean verifyUpdate(String token);

    void updateStoreName(String reference, String storeName);

    SellerViewModel updateAddress(String reference, String address);

    SellerViewModel addLogo(String reference, MultipartFile logoFile);

    int findUserIdByStoreName(String storeName);
}
