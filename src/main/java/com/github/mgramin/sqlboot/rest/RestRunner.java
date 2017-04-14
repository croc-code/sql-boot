package com.github.mgramin.sqlboot.rest;

import static java.lang.System.getenv;
import static java.lang.System.setProperty;
import static java.util.Optional.ofNullable;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestRunner {

    public static void main(String[] args) {
        String profile = ofNullable(getenv("profile")).orElse("information_schema");
        setProperty(ACTIVE_PROFILES_PROPERTY_NAME, profile);
        SpringApplication.run(RestRunner.class, args);
    }

}