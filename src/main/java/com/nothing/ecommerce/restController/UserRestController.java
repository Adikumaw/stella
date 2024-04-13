package com.nothing.ecommerce.restController;

import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.model.UserModel;
import com.nothing.ecommerce.services.UserServiceImpl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserServiceImpl userService;

    public UserRestController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public boolean registerUser(@RequestBody UserModel userModel) {

        return userService.registerUser(userModel);
    }

}
