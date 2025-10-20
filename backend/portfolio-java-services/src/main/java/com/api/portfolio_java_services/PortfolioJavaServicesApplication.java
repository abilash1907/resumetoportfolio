package com.api.portfolio_java_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PortfolioJavaServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioJavaServicesApplication.class, args);
	}

}
