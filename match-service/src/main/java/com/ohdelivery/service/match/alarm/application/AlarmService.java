package com.ohdelivery.service.match.alarm.application;

import com.ohdelivery.service.match.alarm.infrastructure.persistence.JpaAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

  private final JpaAlarmRepository alarmRepository;
}
