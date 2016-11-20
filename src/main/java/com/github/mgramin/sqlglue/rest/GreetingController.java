package com.github.mgramin.sqlglue.rest;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/ddl/**")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name
    ,HttpServletRequest request) {
        String s = request.getRequestURL().toString() + "?" + request.getQueryString();
        System.out.println(s.substring(5));
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

}