package com.nothing.ecommerce.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginCredentials {
    private String email;
    private String number;
    private String password;
    private int active;

    public LoginCredentials(String referance, String password) {
        this.password = password;
    }
}
