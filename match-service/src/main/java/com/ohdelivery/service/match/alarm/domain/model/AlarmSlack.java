package com.ohdelivery.service.match.alarm.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_alarm_slack")
public class AlarmSlack extends BaseEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private UUID alarmId;

  private String slackId;

  private String channelId;

  private String sentAt;

  public static AlarmSlack toEntity(UUID alarmId, String slackId, String channelId,
      String sentAt) {
    return AlarmSlack.builder()
        .alarmId(alarmId)
        .slackId(slackId)
        .channelId(channelId)
        .sentAt(sentAt)
        .build();
  }
}
