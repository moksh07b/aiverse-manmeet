package com.aiverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AIVerseApplication {
	public static void main(String[] args) {
		SpringApplication.run(AIVerseApplication.class, args);
	}
}
