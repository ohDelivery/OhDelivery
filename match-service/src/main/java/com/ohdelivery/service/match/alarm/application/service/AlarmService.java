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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

  private final SlackService slackService;
  private final AlarmRepository alarmRepository;
  private final AlarmRiderRepository alarmRiderRepository;

  @Transactional
  public void sendAlarm(AlarmCommand command) {
    String message = createMessage(command);

    Alarm alarm = Alarm.toEntity(command.getMatchingId(), message);
    alarmRepository.save(alarm);

    // 가용 범위 내 라이더에게 알림
    // slack으로 알림 -> 추후 웹소켓으로 변경 계획 있음
    notifyRidersWithinDistance(command.getSlackIdList(), message);
  }

  private String createMessage(AlarmCommand command) {
    StringBuilder builder = new StringBuilder();

    builder.append("[ 새로운 배달 요청이 도착했습니다! ]\n\n");

    builder.append("가게 정보\n")
        .append("가게 이름: ").append(command.getStoreName()).append("\n")
        .append("가게 주소: ").append(command.getStoreAddress()).append("\n\n");

    builder.append("배달 정보\n")
        .append("배달료: ").append(command.getFee()).append("\n")
        .append("배달지 주소: ").append(command.getTargetAddress()).append("\n")
        .append("배달 요청 사항: ").append(command.getOrderRequest()).append("\n\n");

    return builder.toString();
  }

  private void notifyRidersWithinDistance(List<String> slackEmails, String message) {
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
}
