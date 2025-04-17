package com.ohdelivery.service.user.infrastructure.messaging;


import com.ohdelivery.common.kafka.Topic;
import com.ohdelivery.common.kafka.dto.CreateUserEvent;
import com.ohdelivery.common.kafka.dto.DeleteUserEvent;
import com.ohdelivery.common.kafka.dto.UpdateSlackIdEvent;
import com.ohdelivery.service.user.application.UserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventProducer implements UserEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void produceUpdateSlackId(Long userId, String slackId) {
    kafkaTemplate.send(Topic.UPDATED_SLACK_ID, new UpdateSlackIdEvent(userId, slackId));
  }

  public void produceDeleteUser(Long userId) {
    kafkaTemplate.send(Topic.UPDATE_DELIVERY, new DeleteUserEvent(userId));
  }

  public void produceCreateUser(Long userId, String slackId) {
    kafkaTemplate.send(Topic.CREATED_USER, new CreateUserEvent(userId, slackId));
  }

}
