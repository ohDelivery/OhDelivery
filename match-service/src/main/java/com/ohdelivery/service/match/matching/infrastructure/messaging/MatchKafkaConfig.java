package com.ohdelivery.service.match.matching.infrastructure.messaging;

import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateDeliveryEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
@EnableKafka
public class MatchKafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServerUrl;


  // Kafka 프로듀서 설정
  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(ProducerConfig.ACKS_CONFIG, "all");

    configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // 최대 재시도 횟수
    configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // 재시도 간격 (ms)

    configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
        15000); // 서버 응답 기다리는 최대 시간 (default: 30초)
    configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
        20000); // 전체 전송 타임아웃 (배치 포함, default: 2분)

    configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean(name = "alarmKafkaTemplate")
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  // Kafka Consumer 기본 설정
  @Bean
  public Map<String, Object> deliveryConsumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, MATCH_GROUP_ID);
    return props;
  }

  @Bean
  public ConsumerFactory<String, CreateDeliveryEvent> createDeliveryConsumerFactory() {
    JsonDeserializer<CreateDeliveryEvent> deserializer = new JsonDeserializer<>(
        CreateDeliveryEvent.class, false);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(
        deliveryConsumerConfigs(),
        new StringDeserializer(),
        deserializer
    );
  }

  // Kafka 리스너 팩토리 설정 (리트라이와 DLQ 설정 포함)
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CreateDeliveryEvent> createDeliveryKafkaListenerFactory(
      KafkaTemplate<String, Object> kafkaTemplate
  ) {
    ConcurrentKafkaListenerContainerFactory<String, CreateDeliveryEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    // 기본 컨슈머 팩토리 설정
    factory.setConsumerFactory(createDeliveryConsumerFactory());

    // DLQ로 메시지를 보내는 DeadLetterPublishingRecoverer 생성
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

    // 3번 리트라이 후 DLQ로 메시지를 보내는 DefaultErrorHandler 설정
    DefaultErrorHandler errorHandler = new DefaultErrorHandler(
        recoverer,
        new FixedBackOff(0L, 3L) // 3번 재시도, delay 0ms
    );

    factory.setCommonErrorHandler(errorHandler);

    return factory;
  }
}
