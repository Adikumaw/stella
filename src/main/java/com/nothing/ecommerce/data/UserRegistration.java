package com.nothing.ecommerce.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
    private String userName;
    private String email;
    private String number;
    private String password;
}
