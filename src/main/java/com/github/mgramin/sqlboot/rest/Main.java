package com.github.mgramin.sqlboot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication();
		springApplication.setAdditionalProfiles("information_schema");

		springApplication.run(Main.class, args);
	}

}
