package com.ohdelivery.service.match.alarm.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATE_ALARM;
import static com.ohdelivery.common.kafka.Topic.CREATE_ALARM_DLT;
import static com.ohdelivery.service.match.alarm.infrastructure.config.AlarmKafkaConfig.ALARM_SLACK_GROUP_ID;
import static com.ohdelivery.service.match.alarm.infrastructure.config.AlarmKafkaConfig.ALARM_WEBSOCKET_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateAlarmEvent;
import com.ohdelivery.common.kafka.dto.FailAlarmEvent;
import com.ohdelivery.service.match.alarm.application.service.AlarmService;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventConsumer {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final AlarmService alarmService;

  @KafkaListener(
      topics = CREATE_ALARM,
      groupId = ALARM_SLACK_GROUP_ID,
      containerFactory = "createAlarmKafkaListenerContainerFactory"
  )
  public void notifySlack(CreateAlarmEvent event) throws SlackApiException, IOException {
    try {
      alarmService.notifySlack(event);
    } catch (Exception e) {
      log.error("Slack 알림 처리 실패 (DLT 전송): {}", e.getMessage(), e);
      kafkaTemplate.send(CREATE_ALARM_DLT, FailAlarmEvent.from(event, e));
    }
  }

  @KafkaListener(
      topics = CREATE_ALARM,
      groupId = ALARM_WEBSOCKET_GROUP_ID,
      containerFactory = "createAlarmKafkaListenerContainerFactory"
  )
  public void notifyWebSocket(CreateAlarmEvent event) {
    try {
      alarmService.notifyWebSocket(event);
    } catch (Exception e) {
      log.error("WebSocket 알림 처리 실패 (DLT 전송): {}", e.getMessage(), e);
      kafkaTemplate.send(CREATE_ALARM_DLT, FailAlarmEvent.from(event, e));
    }
  }
}
