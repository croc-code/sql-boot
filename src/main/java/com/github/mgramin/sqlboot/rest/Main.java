package com.github.mgramin.sqlboot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication();
		//application.setAdditionalProfiles("spring.profiles.active=information_schema");

		application.run(Main.class, args);
	}

}
