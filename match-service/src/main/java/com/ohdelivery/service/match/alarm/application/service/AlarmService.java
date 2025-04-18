package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.service.match.alarm.application.dto.AlarmRequest;
import com.ohdelivery.service.match.alarm.application.dto.SlackResponse;
import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.model.AlarmRider;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRiderRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

  private final SlackService slackService;
  private final AlarmRepository alarmRepository;
  private final AlarmRiderRepository alarmRiderRepository;
  private final RiderSessionManager sessionManager;

  @Transactional
  public void sendAlarm(AlarmRequest request) {
    String message = createMessage(request);

    Alarm alarm = Alarm.toEntity(request.getMatchingId(), message);
    alarmRepository.save(alarm);

    notifySlack(request.getSlackIdList(), message);
    notifyWebSocket(request.getRiderIdList(), message);
  }

  private String createMessage(AlarmRequest request) {
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

  private void notifySlack(List<String> slackEmails, String message) {
    for (String slackEmail : slackEmails) {
      try {
        SlackResponse res = slackService.sendSlackMessage(slackEmail, message);
        AlarmRider alarmRider = AlarmRider.toEntity(res.getSlackId(), res.getChannelId(),
            res.getSentAt());
        alarmRiderRepository.save(alarmRider);
      } catch (SlackApiException | IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void notifyWebSocket(List<Long> riderIds, String message) {
    for (Long riderId : riderIds) {
      WebSocketSession session = sessionManager.getSession(riderId.toString());
      if (session != null && session.isOpen()) {
        try {
          session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }
}
