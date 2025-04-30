package com.ohdelivery.service.consult.chat.infrastructure.config;

import com.ohdelivery.service.consult.chat.domain.model.ChatMessage;
import java.util.HashMap;
import java.util.Map;
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

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServerUrl;

  public static final String CHAT_GROUP_ID = "consult-chat";

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ChatMessage> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, ChatMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, ChatMessage> consumerFactory() {
    JsonDeserializer<ChatMessage> deserializer = new JsonDeserializer<>(ChatMessage.class, false);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(
        chatConsumerConfigs(),
        new StringDeserializer(),
        deserializer
    );
  }

  @Bean
  public Map<String, Object> chatConsumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, CHAT_GROUP_ID);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    return props;
  }

}
