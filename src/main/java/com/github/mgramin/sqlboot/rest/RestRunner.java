package com.github.mgramin.sqlboot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class RestRunner {

	public static void main(String[] args) {
		String profile = System.getenv("profile");

		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
		SpringApplication application = new SpringApplication();
		application.run(RestRunner.class, args);

	}

}
