package com.ohdelivery.service.match;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
    "com.ohdelivery.service.match",
    "com.ohdelivery.common"
})
@EnableFeignClients
public class MatchServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MatchServiceApplication.class, args);
  }

}
