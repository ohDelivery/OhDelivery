package com.ohdelivery.service.match.alarm.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Table(name = "p_alarm")
public class Alarm extends BaseEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private UUID deliveryId;

  private String message;

  private String sentAt;
}
