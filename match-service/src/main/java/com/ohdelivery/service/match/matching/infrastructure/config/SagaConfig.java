package com.ohdelivery.service.match.matching.infrastructure.config;

import com.ohdelivery.service.match.common.command.CommandInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SagaConfig {

  @Bean
  public CommandInvoker commandInvoker() {
    return new CommandInvoker();
  }
}
