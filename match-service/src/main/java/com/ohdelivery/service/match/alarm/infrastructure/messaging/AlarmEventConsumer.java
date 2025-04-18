package com.ohdelivery.service.match.alarm.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATED_MATCHING;
import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
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
      topics = CREATED_MATCHING,
      groupId = MATCH_GROUP_ID,
      containerFactory = "createMatchingKafkaListenerContainerFactory"
  )
  public void sendAlarm(CreateMatchingEvent event) {
    AlarmEvent alarmEvent = AlarmEvent.from(event);
    alarmService.sendAlarm(alarmEvent);
  }
}
