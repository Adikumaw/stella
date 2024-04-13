package com.nothing.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginModel {
    private String email;
    private String number;
    private String password;
    private int active;

    public UserLoginModel(String referance, String password) {
        this.password = password;
    }
}
