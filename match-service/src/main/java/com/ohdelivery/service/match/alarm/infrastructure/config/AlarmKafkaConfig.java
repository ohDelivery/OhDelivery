package com.ohdelivery.service.match.alarm.infrastructure.config;

import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateAlarmEvent;
import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.common.kafka.dto.FailAlarmEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Slf4j
@Configuration
@EnableKafka
public class AlarmKafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServerUrl;

  public static final String ALARM_SLACK_GROUP_ID = "alarm-slack";
  public static final String ALARM_WEBSOCKET_GROUP_ID = "alarm-websocket";
  public static final String ALARM_DLT_GROUP_ID = "alarm-dlt-consumer";

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public AlarmKafkaConfig(
      @Qualifier("alarmKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Bean
  public ConsumerFactory<String, CreateMatchingEvent> createMatchingConsumerFactory() {
    JsonDeserializer<CreateMatchingEvent> des = new JsonDeserializer<>(CreateMatchingEvent.class);
    des.addTrustedPackages("*");
    des.setRemoveTypeHeaders(false);
    des.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, MATCH_GROUP_ID);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), des);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CreateMatchingEvent> createMatchingKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, CreateMatchingEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(createMatchingConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, CreateAlarmEvent> createAlarmConsumerFactory() {
    JsonDeserializer<CreateAlarmEvent> des = new JsonDeserializer<>(CreateAlarmEvent.class);
    des.addTrustedPackages("*");
    des.setRemoveTypeHeaders(false);
    des.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), des);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CreateAlarmEvent> createAlarmKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, CreateAlarmEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(createAlarmConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, FailAlarmEvent> alarmDltConsumerFactory() {
    JsonDeserializer<FailAlarmEvent> des = new JsonDeserializer<>(FailAlarmEvent.class);
    des.addTrustedPackages("*");
    des.setRemoveTypeHeaders(false);
    des.setUseTypeMapperForKey(true);

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, ALARM_DLT_GROUP_ID);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), des);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, FailAlarmEvent> alarmDltKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, FailAlarmEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(alarmDltConsumerFactory());
    factory.setCommonErrorHandler(new DefaultErrorHandler());
    return factory;
  }
}
