package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.common.kafka.dto.CreateMatchingEvent;
import com.ohdelivery.service.match.alarm.application.dto.AlarmRequest;
import com.ohdelivery.service.match.alarm.application.dto.SlackResponse;
import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.model.AlarmSlack;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRiderRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.List;
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
  private final AlarmRiderRepository alarmRiderRepository;

  @Transactional
  public void sendAlarm(AlarmRequest request) {
    Alarm alarm = Alarm.toEntity(request.getMatchingId(), request.getMessage());
    alarmRepository.save(alarm);

    notifySlack(request.getSlackIdList(), request.getMessage());
    notifyWebSocket(request.getRiderIdList(), request.getMessage());
  }

  private void notifySlack(List<String> slackEmails, String message) {
    for (String slackEmail : slackEmails) {
      try {
        SlackResponse res = slackService.sendSlackMessage(slackEmail, message);
        AlarmSlack alarmRider = AlarmSlack.toEntity(res.getSlackId(), res.getChannelId(),
            res.getSentAt());
        alarmRiderRepository.save(alarmRider);
      } catch (SlackApiException | IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void notifyWebSocket(List<Long> riderIds, String message) {
    for (Long riderId : riderIds) {
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
