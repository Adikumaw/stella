package com.nothing.ecommerce.restController;

import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.model.UserModel;
import com.nothing.ecommerce.services.UserServiceImpl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserServiceImpl userService;

    public UserRestController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel) {

        System.out.println("in register user controller");

        userService.registerUser(userModel);

        return "success";
    }

    @GetMapping("/hello")
    public String hello_world() {
        return "hello world!";
    }

}
