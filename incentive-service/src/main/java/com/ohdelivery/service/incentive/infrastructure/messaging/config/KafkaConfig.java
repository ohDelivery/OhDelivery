package com.ohdelivery.service.incentive.infrastructure.messaging.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.StreamsBuilderFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import lombok.extern.slf4j.Slf4j;

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

	//스트림 config
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "incentive-stream-app");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());
		props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
		props.put(StreamsConfig.METRICS_RECORDING_LEVEL_CONFIG, "DEBUG");
		return new KafkaStreamsConfiguration(props);
	}

	//스트림용 에러핸들러
	@Bean
	public StreamsBuilderFactoryBeanCustomizer streamsBuilderFactoryBeanCustomizer() {
		return factoryBean -> factoryBean.setStreamsUncaughtExceptionHandler(exception -> {
			log.error("Kafka Streams 처리 중 예외 발생: {}", exception.getMessage(), exception);
			// SHUTDOWN_CLIENT: 스트림 종료, REPLACE_THREAD: 스레드 교체
			return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
		});
	}

	@Bean
	public KafkaStreams kafkaStreams(StreamsBuilderFactoryBean factoryBean) throws Exception {
		return factoryBean.getKafkaStreams();
	}

	@Bean
	public KStream<String, byte[]> exampleStream(StreamsBuilder builder) {
		log.info("✅ Kafka Streams Topology 생성됨");
		KStream<String, byte[]> stream = builder.stream("delivery-record-create");

		// 간단히 peek만 해도 스트림 생성됨 (안 쓰면 최적화돼서 무시됨)
		stream.peek((key, value) -> log.info("KafkaStream 수신: key={}, value={}", key, value));

		return stream;
	}

}

