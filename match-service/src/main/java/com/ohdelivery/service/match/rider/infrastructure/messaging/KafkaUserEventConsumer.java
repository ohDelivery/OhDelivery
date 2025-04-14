package com.ohdelivery.service.match.rider.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.DELETED_USER;
import static com.ohdelivery.common.kafka.Topic.UPDATED_SLACK_ID;
import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.DeleteUserEvent;
import com.ohdelivery.common.kafka.dto.UpdateSlackIdEvent;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaUserEventConsumer {

  private final RiderService riderService;

  @KafkaListener(
      topics = UPDATED_SLACK_ID,
      groupId = MATCH_GROUP_ID,
      containerFactory = "updateSlackIdKafkaListenerFactory"
  )
  public void consumeUpdateSlackId(UpdateSlackIdEvent event) {
    log.info("Received UpdateSlackIdEvent: userId={}, slackId={}", event.getUserId(),
        event.getSlack_id());
    riderService.updateSlackId(event.getUserId(), event.getSlack_id());
  }

  @KafkaListener(
      topics = DELETED_USER,
      groupId = MATCH_GROUP_ID,
      containerFactory = "deleteUserKafkaListenerFactory"
  )
  public void consumeDeleteUser(DeleteUserEvent event) {
    log.info("Received DeleteUserEvent: userId={}", event.getUserId());
    riderService.deleteRiderByRiderId(event.getUserId());
  }
}
