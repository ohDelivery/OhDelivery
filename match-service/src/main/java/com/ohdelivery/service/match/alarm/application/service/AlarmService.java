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
    Alarm alarm = Alarm.toEntity(request.getMatchingId(), request.getMessage());
    alarmRepository.save(alarm);

    notifySlack(request.getSlackIdList(), request.getMessage());
    notifyWebSocket(request.getRiderIdList(), request.getMessage());
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
