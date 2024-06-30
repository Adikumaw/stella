package com.nothing.ecommerce.model;

import com.nothing.ecommerce.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserViewModel {
    private String name;
    private String email;
    private String number;
    private int status;

    public UserViewModel(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.number = user.getNumber();
        this.status = user.getActive();
    }
}
