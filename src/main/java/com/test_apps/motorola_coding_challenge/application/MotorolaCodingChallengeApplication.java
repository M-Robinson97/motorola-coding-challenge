package com.test_apps.motorola_coding_challenge.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MotorolaCodingChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotorolaCodingChallengeApplication.class, args);
	}

}
