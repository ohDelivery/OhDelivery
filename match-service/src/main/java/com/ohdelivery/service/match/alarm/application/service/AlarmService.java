package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.common.kafka.dto.CreateAlarmEvent;
import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.service.match.alarm.application.dto.response.SlackResponse;
import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.model.AlarmSlack;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmSlackRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

  private final SlackService slackService;
  private final SimpMessagingTemplate messagingTemplate;
  private final AlarmRepository alarmRepository;
  private final AlarmSlackRepository alarmSlackRepository;

  @Transactional
  public UUID saveAlarm(UUID matchingId, String message) {
    Alarm alarm = Alarm.toEntity(matchingId, message);
    Alarm saveAlarm = alarmRepository.save(alarm);
    return saveAlarm.getId();
  }

  @Transactional
  public void notifySlack(CreateAlarmEvent event) {
    for (String slackEmail : event.getSlackEmails()) {
      try {
        SlackResponse res = slackService.sendSlackMessage(slackEmail, event.getMessage());
        AlarmSlack alarmSlack = AlarmSlack.toEntity(event.getAlarmId(), res.getSlackId(),
            res.getChannelId(), res.getSentAt());
        alarmSlackRepository.save(alarmSlack);
        log.info("Slack 메시지 전송 완료: slackEmail={}", slackEmail);
      } catch (SlackApiException | IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  public void notifyWebSocket(CreateAlarmEvent event) {
    String message = event.getMessage();
    for (Long riderId : event.getRiderIds()) {
      messagingTemplate.convertAndSendToUser(riderId.toString(), "/topic/alarm/", message);
      log.info("WebSocket 메시지 전송 완료: userId={}", riderId);
    }
  }

  public String createMessage(CreateMatchingEvent event) {
    StringBuilder builder = new StringBuilder();

    builder.append("[\uD83D\uDD14 새로운 배달 요청이 도착했습니다! ]\n\n");

    builder.append("\uD83C\uDFE1 <").append(event.getStoreName()).append(">\n")
        .append("- 가게 주소: ").append(event.getStoreAddress()).append("\n\n");

    builder.append("\uD83C\uDF73 배달 정보\n")
        .append("- 배달료: ").append(event.getFee()).append("\n")
        .append("- 배달지 주소: ").append(event.getTargetAddress()).append("\n")
        .append("- 요청 사항: ").append(event.getOrderRequest());

    return builder.toString();
  }
}
