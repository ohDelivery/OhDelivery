package com.ohdelivery.service.match.alarm.domain.repository;

import com.ohdelivery.service.match.alarm.domain.model.AlarmSlack;

public interface AlarmRiderRepository {

  AlarmSlack save(AlarmSlack alarmRider);
}
