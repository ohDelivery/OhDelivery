package com.ohdelivery.service.delivery.infrastructure.config;

import com.ohdelivery.common.kafka.dto.CompleteMatchingEvent;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@Slf4j
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.url}")
    private String kafkaServerUrl;

    public static final String DELIVERY_GROUP_ID = "delivery-service";

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");

        // JsonSerializer가 타입 정보를 유지하도록 설정
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS,
            false); // 기본값 true인데, DTO를 수신 측에서 타입 명시 없이 받으려면 false로도 가능

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, CompleteMatchingEvent> completeMatchingConsumerFactory() {
        JsonDeserializer<CompleteMatchingEvent> deserializer = new JsonDeserializer<>(
            CompleteMatchingEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, DELIVERY_GROUP_ID);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CompleteMatchingEvent> completeMatchingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CompleteMatchingEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(completeMatchingConsumerFactory());
        return factory;
    }
}
