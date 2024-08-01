package com.nothing.stella.model;

import java.util.Date;

import com.nothing.stella.entity.Seller;
import com.nothing.stella.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SellerViewModel {
    private String name;
    private String email;
    private String number;
    private int status;
    private String storeName;
    private String address;
    private Date createdAt;

    public SellerViewModel(User user, Seller seller) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.number = user.getNumber();
        this.status = user.isActive() ? 1 : 0;
        this.storeName = seller.getStoreName();
        this.address = seller.getAddress();
        this.createdAt = seller.getCreatedAt();
    }
}
