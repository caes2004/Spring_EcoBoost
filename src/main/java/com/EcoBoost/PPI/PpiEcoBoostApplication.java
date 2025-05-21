package com.EcoBoost.PPI;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PpiEcoBoostApplication {

	public static void main(String[] args) {
		Dotenv env = Dotenv.load();
		env.get("DB_NAME"); 
		env.get("DB_USERNAME");
		env.get("DB_PASSWORD");
		env.get("SPRING_SEC_USER");
		env.get("SPRING_SEC_PASSWORD");
		System.setProperty("SPRING_SEC_PASSWORD", env.get("SPRING_SEC_PASSWORD"));
		SpringApplication.run(PpiEcoBoostApplication.class, args);
	}


}
