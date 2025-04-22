package com.ohdelivery.service.match.alarm.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "p_alarm")
public class Alarm extends BaseEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private UUID matchingId;

  private String message;

  @OneToMany(mappedBy = "alarm", fetch = FetchType.LAZY)
  private List<AlarmSlack> alarmRider;

  public static Alarm toEntity(UUID matchingId, String message) {
    return Alarm.builder()
        .matchingId(matchingId)
        .message(message)
        .alarmRider(new ArrayList<>())
        .build();
  }
}
