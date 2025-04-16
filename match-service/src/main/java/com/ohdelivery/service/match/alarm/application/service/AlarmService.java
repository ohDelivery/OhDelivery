package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.service.match.alarm.application.dto.AlarmCommand;
import com.ohdelivery.service.match.alarm.application.dto.SlackResponse;
import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.model.AlarmRider;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRiderRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

  private final SlackService slackService;
  private final AlarmRepository alarmRepository;
  private final AlarmRiderRepository alarmRiderRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @Transactional
  public void sendAlarm(AlarmCommand command) {
    String message = createMessage(command);

    Alarm alarm = Alarm.toEntity(command.getMatchingId(), message);
    alarmRepository.save(alarm);

    notifySlack(command.getSlackIdList(), message);
    notifyWebSocket(message);
  }

  private String createMessage(AlarmCommand command) {
    StringBuilder builder = new StringBuilder();

    builder.append("[\uD83D\uDD14 새로운 배달 요청이 도착했습니다! ]\n\n");

    builder.append("\uD83C\uDFE1 <").append(command.getStoreName()).append(">\n")
        .append("- 가게 주소: ").append(command.getStoreAddress()).append("\n\n");

    builder.append("\uD83C\uDF73 배달 정보\n")
        .append("- 배달료: ").append(command.getFee()).append("\n")
        .append("- 배달지 주소: ").append(command.getTargetAddress()).append("\n")
        .append("- 요청 사항: ").append(command.getOrderRequest());

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
        // todo: 예외 처리 로직 추가
      }
    }
  }

  private void notifyWebSocket(String message) {
    messagingTemplate.convertAndSend("/topic/alarm", message);
  }
}
