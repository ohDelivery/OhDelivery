package com.ohdelivery.service.match.matching.domain;

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
@Table(name = "p_matching")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Matching extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
  private UUID id;

  @Column(name = "rider_id")
  private UUID riderId;

  @Column(name = "deliveryId", nullable = false)
  private UUID deliveryId;

  public void assignRider(UUID riderId) {
    if (isUpdatable()) {
      this.riderId = riderId;
    } else {
      throw new IllegalStateException("이미 라이더가 배정된 매칭입니다.");
    }
  }

  public static Matching create(UUID deliveryId) {
    Matching matching = new Matching();
    matching.deliveryId = deliveryId;
    return matching;
  }

  public boolean isUpdatable() {
    return this.riderId == null;
  }


}
