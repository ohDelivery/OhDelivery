package com.ohdelivery.service.match.alarm.domain.repository;

import com.ohdelivery.service.match.alarm.domain.model.Alarm;

public interface AlarmRepository {

  Alarm save(Alarm alarm);
}
