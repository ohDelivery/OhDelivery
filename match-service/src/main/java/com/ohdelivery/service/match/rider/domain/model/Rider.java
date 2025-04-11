package com.ohdelivery.service.match.rider.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_rider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Rider extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id",nullable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
  private UUID id;

  @Column(name = "rider_id")
  private Integer riderId;

  @Column(name = "slack_id")
  private String slackId;

  @Column(name = "status")
  private RiderStatus status;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  public Rider(Integer riderId, String slackId) {
    this.riderId = riderId;
    this.slackId = slackId;
    this.status = RiderStatus.OFFLINE;
    this.latitude = null;
    this.longitude = null;
  }

  public void changeStatus(RiderStatus status) {
    this.status=status;
  }
}
