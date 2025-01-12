package com.example.ahriManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AhriManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhriManagerApplication.class, args);
	}

}
