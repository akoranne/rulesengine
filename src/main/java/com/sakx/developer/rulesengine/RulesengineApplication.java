package com.sakx.developer.rulesengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RulesengineApplication {

	@RequestMapping("/")
	public String home() {
		return "\n\n	*** Rules Engine *** ";
	}

	public static void main(String[] args) {
		SpringApplication.run(RulesengineApplication.class, args);
	}
}
