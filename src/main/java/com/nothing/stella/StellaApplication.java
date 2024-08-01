package com.nothing.stella;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StellaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StellaApplication.class, args);

		// TODO check for order conformation after every 10 minutes
	}
}
