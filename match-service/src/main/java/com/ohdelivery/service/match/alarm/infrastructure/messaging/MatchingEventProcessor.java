package com.ohdelivery.service.match.alarm.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATED_MATCHING;
import static com.ohdelivery.common.kafka.Topic.CREATE_ALARM;
import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateAlarmEvent;
import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.service.match.alarm.application.service.AlarmService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingEventProcessor {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final AlarmService alarmService;

  @KafkaListener(
      topics = CREATED_MATCHING,
      groupId = MATCH_GROUP_ID,
      containerFactory = "createMatchingKafkaListenerContainerFactory"
  )
  public void saveAlarm(CreateMatchingEvent event) {
    String message = alarmService.createMessage(event);
    UUID alarmId = alarmService.saveAlarm(event.getMatchingId(), message);
    CreateAlarmEvent alarmEvent = CreateAlarmEvent.from(
        alarmId, message, event.getSlackIdList(), event.getRiderIdList());
    kafkaTemplate.send(CREATE_ALARM, alarmEvent);
  }
}
