package com.github.mgramin.sqlboot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class RestRunner {

	public static void main(String[] args) {
//		String profile = "postgres";
		String profile = "information_schema";

		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
		SpringApplication application = new SpringApplication();
		application.run(RestRunner.class, args);
	}

}
