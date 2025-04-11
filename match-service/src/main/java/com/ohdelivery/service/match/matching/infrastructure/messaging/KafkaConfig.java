package com.ohdelivery.service.match.matching.infrastructure.messaging;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@Slf4j
@EnableKafka
public class KafkaConfig {
  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServerUrl;

  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(ProducerConfig.ACKS_CONFIG, "all");

    configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // 최대 재시도 횟수
    configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // 재시도 간격 (ms)

    configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15000); // 서버 응답 기다리는 최대 시간 (default: 30초)
    configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 20000); // 전체 전송 타임아웃 (배치 포함, default: 2분)

//    configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 배치당 최대 바이트 (default: 16KB)
//    configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);      // 배치 대기 시간 (ms, default: 0)

    configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
