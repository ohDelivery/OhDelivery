package com.ohdelivery.service.match.alarm.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATE_ALARM;
import static com.ohdelivery.service.match.alarm.infrastructure.config.AlarmKafkaConfig.ALARM_SLACK_GROUP_ID;
import static com.ohdelivery.service.match.alarm.infrastructure.config.AlarmKafkaConfig.ALARM_WEBSOCKET_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateAlarmEvent;
import com.ohdelivery.service.match.alarm.application.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmEventConsumer {

  private final AlarmService alarmService;

  @KafkaListener(
      topics = CREATE_ALARM,
      groupId = ALARM_SLACK_GROUP_ID,
      containerFactory = "createAlarmKafkaListenerContainerFactory"
  )
  public void notifySlack(CreateAlarmEvent event) {
    alarmService.notifySlack(event);
  }

  @KafkaListener(
      topics = CREATE_ALARM,
      groupId = ALARM_WEBSOCKET_GROUP_ID,
      containerFactory = "createAlarmKafkaListenerContainerFactory"
  )
  public void notifyWebSocket(CreateAlarmEvent event) {
    alarmService.notifyWebSocket(event);
  }
}
