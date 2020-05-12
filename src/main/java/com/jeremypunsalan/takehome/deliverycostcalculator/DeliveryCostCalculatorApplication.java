package com.jeremypunsalan.takehome.deliverycostcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.jeremypunsalan.takehome.deliverycostcalculator")
@SpringBootApplication
public class DeliveryCostCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryCostCalculatorApplication.class, args);
	}

}
