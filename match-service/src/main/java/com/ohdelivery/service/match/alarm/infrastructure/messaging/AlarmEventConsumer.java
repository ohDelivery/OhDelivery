package com.ohdelivery.service.match.alarm.infrastructure.messaging;

import static com.ohdelivery.common.kafka.Topic.CREATED_MATCHING;
import static com.ohdelivery.service.match.rider.infrastructure.messaging.RiderKafkaConfig.MATCH_GROUP_ID;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.service.match.alarm.application.dto.AlarmRequest;
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
    String message = createMessage(event);
    AlarmRequest request = AlarmRequest.from(event, message);
    alarmService.sendAlarm(request);
  }

  private String createMessage(CreateMatchingEvent request) {
    StringBuilder builder = new StringBuilder();

    builder.append("[\uD83D\uDD14 새로운 배달 요청이 도착했습니다! ]\n\n");

    builder.append("\uD83C\uDFE1 <").append(request.getStoreName()).append(">\n")
        .append("- 가게 주소: ").append(request.getStoreAddress()).append("\n\n");

    builder.append("\uD83C\uDF73 배달 정보\n")
        .append("- 배달료: ").append(request.getFee()).append("\n")
        .append("- 배달지 주소: ").append(request.getTargetAddress()).append("\n")
        .append("- 요청 사항: ").append(request.getOrderRequest());

    return builder.toString();
  }
}
