package com.ohdelivery.service.match.alarm.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "p_alarm_rider")
public class AlarmSlack extends BaseEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private String slackId;

  private String channelId;

  private String sentAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "alarm_id")
  private Alarm alarm;

  public static AlarmSlack toEntity(String slackId, String channelId, String sentAt) {
    return AlarmSlack.builder()
        .slackId(slackId)
        .channelId(channelId)
        .sentAt(sentAt)
        .build();
  }
}
