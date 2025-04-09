package com.ohdelivery.service.match.rider.domain.model;

public enum RiderStatus {
  AVAILABLE(true),       // 배달 지정 가능
  DELIVERING(false),     // 현재 배달 중
  RESTING(false),        // 휴식 중
  OFFLINE(false),        // 오프라인
  BLOCKED(false);        // 시스템에 의해 제한된 상태

  private final boolean assignable;

  RiderStatus(boolean assignable) {
    this.assignable = assignable;
  }

  public boolean isAssignable() {
    return assignable;
  }
}
