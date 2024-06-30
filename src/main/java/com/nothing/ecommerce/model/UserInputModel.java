package com.nothing.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputModel {
    private String name;
    private String email;
    private String number;
    private String password;

    public UserInputModel(SellerInputModel model) {
        this.name = model.getName();
        this.email = model.getEmail();
        this.number = model.getNumber();
        this.password = model.getPassword();
    }
}
