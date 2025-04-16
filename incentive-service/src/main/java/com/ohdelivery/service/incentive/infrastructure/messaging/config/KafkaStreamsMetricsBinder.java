package com.ohdelivery.service.incentive.infrastructure.messaging.config;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStreamsMetricsBinder {

	private final StreamsBuilderFactoryBean factoryBean;
	private final MeterRegistry meterRegistry;

	@EventListener(ApplicationReadyEvent.class)
	public void bindKafkaStreamsMetrics() {
		KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
		if (kafkaStreams != null) {
			log.info("✅ KafkaStreamsMetrics 등록됨");
			new KafkaStreamsMetrics(kafkaStreams).bindTo(meterRegistry);
			meterRegistry.getMeters().stream()
				.map(m -> m.getId().getName())
				.filter(name -> name.contains("kafka"))
				.forEach(name -> log.info("📈 등록된 kafka 메트릭: {}", name));
		}
	}
}

