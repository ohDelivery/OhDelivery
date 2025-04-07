package com.ohdelivery.service.incentive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IncentiveServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(IncentiveServiceApplication.class, args);
	}
}