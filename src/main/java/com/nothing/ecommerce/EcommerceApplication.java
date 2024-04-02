package com.nothing.ecommerce;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nothing.ecommerce.services.UserService;
import com.nothing.ecommerce.entity.*;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserService userService) {
		return runner -> {
			UserDetails userDetails = new UserDetails("aditya", "kumawataditya", "7877595234");

			userService.saveUser(userDetails);
		};
	}
}
