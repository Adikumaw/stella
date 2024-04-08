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

			// User myUser = new User("gaurav kuma", "helloworld@gmail.com", "9829475206",
			// "insecurity");
			// userService.registerUser(myUser);

			User myUser = userService.findUser("8290111126");
			if (myUser != null) {
				UserAddress userAddress = new UserAddress("2004 tikki walo ka rasta, kishanpole bazar", "jaipur",
						"Rajasthan", "302012", "India", myUser);
				userService.saveUserAddress(userAddress);
			}
		};
	}
}
