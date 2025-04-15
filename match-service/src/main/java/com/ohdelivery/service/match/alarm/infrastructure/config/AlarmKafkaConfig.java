package com.ohdelivery.service.match.alarm.infrastructure.config;

import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Slf4j
@Configuration
@EnableKafka
public class AlarmKafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServerUrl;

  @Bean
  public ConsumerFactory<String, CreateMatchingEvent> createMatchingConsumerFactory() {
    JsonDeserializer<CreateMatchingEvent> deserializer = new JsonDeserializer<>(
        CreateMatchingEvent.class);
    deserializer.addTrustedPackages("*");
    deserializer.setRemoveTypeHeaders(false);
    deserializer.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, MATCH_GROUP_ID);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CreateMatchingEvent> createMatchingKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, CreateMatchingEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(createMatchingConsumerFactory());
    return factory;
  }
}
