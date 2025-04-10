package com.ohdelivery.service.match.alarm.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${message.exchange}")
  private String exchange;

  @Value("${message.queue}")
  private String queueMatch;

  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(exchange);
  }

  @Bean
  public Queue queueMatch() {
    return new Queue(queueMatch);
  }

  @Bean
  public Binding bindingProduct() {
    return BindingBuilder.bind(queueMatch()).to(exchange()).with(queueMatch);
  }
}
