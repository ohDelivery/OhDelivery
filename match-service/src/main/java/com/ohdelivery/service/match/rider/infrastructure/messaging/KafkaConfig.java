package com.ohdelivery.service.match.rider.infrastructure.messaging;

import com.ohdelivery.common.kafka.dto.DeleteUserEvent;
import com.ohdelivery.common.kafka.dto.UpdateSlackIdEvent;
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

@Configuration
@Slf4j
@EnableKafka
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServerUrl;

  public static final String MATCH_GROUP_ID = "rider-service";


  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    // true면 JSON 객체로 안전하게 역직렬화 가능
    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, MATCH_GROUP_ID);
    return props;
  }

  @Bean
  public ConsumerFactory<String, UpdateSlackIdEvent> updateSlackIdConsumerFactory() {
    JsonDeserializer<UpdateSlackIdEvent> deserializer = new JsonDeserializer<>(
        UpdateSlackIdEvent.class, false);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(
        consumerConfigs(),
        new StringDeserializer(),
        deserializer
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, UpdateSlackIdEvent> updateSlackIdKafkaListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, UpdateSlackIdEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(updateSlackIdConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, DeleteUserEvent> deleteUserConsumerFactory() {
    JsonDeserializer<DeleteUserEvent> deserializer = new JsonDeserializer<>(
        DeleteUserEvent.class, false);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(
        consumerConfigs(),
        new StringDeserializer(),
        deserializer
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, DeleteUserEvent> deleteUserKafkaListenerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, DeleteUserEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(deleteUserConsumerFactory());
    return factory;
  }
}
