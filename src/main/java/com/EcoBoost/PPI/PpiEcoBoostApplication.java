package com.EcoBoost.PPI;

import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PpiEcoBoostApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpiEcoBoostApplication.class, args);
	}


}
