package com.ohdelivery.service.consult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(
    scanBasePackages = {"com.ohdelivery.service.consult", "com.ohdelivery.common"}
)
public class ConsultServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsultServiceApplication.class, args);
  }

}
