package com.ohdelivery.service.match.alarm.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATE_ALARM_DLT;
import static com.ohdelivery.service.match.alarm.infrastructure.config.AlarmKafkaConfig.ALARM_DLT_GROUP_ID;

import com.ohdelivery.common.kafka.dto.FailAlarmEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmDltConsumer {

  private final Counter alarmFailCounter = Counter.builder("alarm_dlt_failures_total")
      .description("DLT 알람 전송 실패 횟수")
      .tag("alarm-fail-dlt", "match-service")
      .register(Metrics.globalRegistry);

  @KafkaListener(
      topics = CREATE_ALARM_DLT,
      groupId = ALARM_DLT_GROUP_ID,
      containerFactory = "alarmDltKafkaListenerContainerFactory"
  )
  public void handleFailAlarm(FailAlarmEvent event) {
    log.error("알람 전송 실패: {}", event.getErrorMessage());
    alarmFailCounter.increment();
  }
}
