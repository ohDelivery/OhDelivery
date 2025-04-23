package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.service.match.alarm.application.dto.AlarmRequest;
import com.ohdelivery.service.match.alarm.application.dto.SlackResponse;
import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.model.AlarmSlack;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmSlackRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
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
  public void sendAlarm(AlarmRequest req) {
    Alarm alarm = Alarm.toEntity(req.getMatchingId(), req.getMessage());
    alarmRepository.save(alarm);

    notifySlack(req);
    notifyWebSocket(req);
  }

  private void notifySlack(AlarmRequest req) {
    for (String slackEmail : req.getSlackIdList()) {
      try {
        SlackResponse res = slackService.sendSlackMessage(slackEmail, req.getMessage());
        AlarmSlack alarmSlack = AlarmSlack.toEntity(req.getMatchingId(), res.getSlackId(),
            res.getChannelId(), res.getSentAt());
        alarmSlackRepository.save(alarmSlack);
        log.info("Slack 메시지 전송 완료: slackEmail={}", slackEmail);
      } catch (SlackApiException | IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void notifyWebSocket(AlarmRequest req) {
    for (Long riderId : req.getRiderIdList()) {
      messagingTemplate.convertAndSendToUser(riderId.toString(), "/topic/alarm/", req.getMessage());
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
