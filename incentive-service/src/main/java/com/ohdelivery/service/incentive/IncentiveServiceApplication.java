package com.ohdelivery.service.incentive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.ohdelivery.service.incentive",
	"com.ohdelivery.common"})
@EnableKafkaStreams
public class IncentiveServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(IncentiveServiceApplication.class, args);
	}
}