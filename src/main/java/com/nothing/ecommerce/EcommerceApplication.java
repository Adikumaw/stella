package com.nothing.ecommerce;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nothing.ecommerce.services.UserService;
import com.nothing.ecommerce.data.UserRegistration;
import com.nothing.ecommerce.entity.*;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	@Autowired
	public CommandLineRunner commandLineRunner(UserService userService) {
		return runner -> {

			System.out.println(userService.getUser(1));

			UserRegistration userRegistration = new UserRegistration("banti", "", "8529198984",
					"nothing_Phone2a");

			UserDetails userDetails = new UserDetails(userRegistration.getUserName(), userRegistration.getEmail(),
					userRegistration.getNumber());

			userService.saveUser(userDetails);

			LoginCredentials loginC = new LoginCredentials(userRegistration.getPassword());
		};
	}
}
