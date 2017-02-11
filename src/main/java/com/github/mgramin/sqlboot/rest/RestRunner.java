package com.github.mgramin.sqlboot.rest;

import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class RestRunner {

	public static void main(String[] args) {

		String profile = Optional.ofNullable(System.getenv("profile"))
				.map(v -> v)
				.orElse("information_schema");

		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
		SpringApplication application = new SpringApplication();
		application.run(RestRunner.class, args);

	}

}
