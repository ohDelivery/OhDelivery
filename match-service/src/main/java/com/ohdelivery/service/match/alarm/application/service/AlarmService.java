package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.service.match.alarm.application.dto.AlarmEvent;
import com.ohdelivery.service.match.alarm.application.dto.SlackResponse;
import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.model.AlarmRider;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRiderRepository;
import com.ohdelivery.service.match.rider.domain.repository.RiderRepository;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

  private final SlackService slackService;
  private final AlarmRepository alarmRepository;
  private final AlarmRiderRepository alarmRiderRepository;
  private final RiderRepository riderRepository;

  private static final double EARTH_RADIUS = 6371.0;

  @Transactional
  @RabbitListener(queues = "${message.queue}")
  public void sendAlarm(AlarmEvent event) {

    Double storeLat = event.getStoreLatitude();
    Double storeLong = event.getStoreLongitude();
    String message = createMessage(event);

    Alarm alarm = Alarm.toEntity(event.getDeliveryId(), message);
    alarmRepository.save(alarm);

    // 가용 범위 내 라이더에게 알림
    // slack으로 알림 -> 추후 웹소켓으로 변경 계획 있음
    notifyRidersWithinDistance(storeLat, storeLong, message);
  }

  private String createMessage(AlarmEvent event) {
    StringBuilder builder = new StringBuilder();

    builder.append("[ 새로운 배달 요청이 도착했습니다! ]\n\n");
    builder.append("가게 주소: ").append(event.getStoreAddress()).append("\n");

    return builder.toString();
  }

  private void notifyRidersWithinDistance(Double storeLat, Double storeLong, String message) {
    // todo: riderService.getRiders() 메서드 구현 요청
    riderRepository.findAll().stream()
        .filter(rider -> rider.getStatus().equals("AVAILABLE"))
        .forEach(rider -> {
          double distance = calculateDistance(storeLat, storeLong, rider.getLatitude(),
              rider.getLongitude());
          if (distance <= 10) {
//            String slackEmail = rider.getSlackEmail();
            String slackEmail = "@gmail.com";
            try {
              SlackResponse res = slackService.sendSlackMessage(slackEmail, message);
              AlarmRider alarmRider = AlarmRider.toEntity(rider.getRiderId(), res.getSlackId(),
                  res.getChannelId(), res.getSentAt());
              alarmRiderRepository.save(alarmRider);
            } catch (SlackApiException | IOException e) {
              // todo: 예외 처리 로직 추가
            }
          }
        });
  }

  // 우선 직접 거리 계산하는 방식으로 구현했으나 추후 Redis Geo 등 도입하여 성능 개선 예정
  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS * c;
  }
}
