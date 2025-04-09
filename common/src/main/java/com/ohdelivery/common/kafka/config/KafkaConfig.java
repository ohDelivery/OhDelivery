package com.ohdelivery.common.kafka.config;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;


@Configuration
@Slf4j
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

	@Value("${spring.kafka.url}")
	private String kafkaServerUrl;

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(ProducerConfig.ACKS_CONFIG, "all");

		// JsonSerializer가 타입 정보를 유지하도록 설정
		configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false); // 기본값 true인데, DTO를 수신 측에서 타입 명시 없이 받으려면 false로도 가능

		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		// Kafka 서버 주소 설정
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
		// 컨슈머 그룹 ID 설정
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "ohDelivery-group");
		// 키 역직렬화 설정 (String 타입)
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		// 값 역직렬화 설정 (JSON 타입)
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		// 자동 커밋 설정
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		// 처음 시작 시 가장 오래된 메시지부터 읽음
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		// 실제 역직렬화기 지정
		props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
		props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

		// JsonDeserializer 관련 설정
		// 모든 패키지의 클래스 역직렬화 허용 (보안에 주의)
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		// 타입 정보가 없는 경우 Map으로 변환
		props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "java.util.Map");

		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		// 컨슈머 팩토리 설정
		factory.setConsumerFactory(consumerFactory());
		// 동시에 처리할 수 있는 스레드 수 (파티션 수에 맞게 조정)
		factory.setConcurrency(3);
		// 배치 리스너 비활성화 (개별 메시지 처리)
		factory.setBatchListener(false);

		// 에러 핸들러 수정 - 재시도 없이 로깅만 하도록 설정
		DefaultErrorHandler errorHandler = new DefaultErrorHandler((record, exception) -> {
			log.error("메시지 처리 실패: {}", exception.getMessage());
			log.error("실패한 메시지: {}", record);
			// 필요한 추가 작업 수행 (예: 데드 레터 큐로 전송)
		}, new FixedBackOff(3L, 3L)); // 재시도 3회 반복

		// SerializationException도 처리하도록 설정
		errorHandler.addNotRetryableExceptions(org.apache.kafka.common.errors.SerializationException.class);

		factory.setCommonErrorHandler(errorHandler);

		return factory;
	}

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "incentive-stream-app");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());
		props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
		return new KafkaStreamsConfiguration(props);
	}
}

