package com.ohdelivery.service.match.alarm.presetation;

import com.ohdelivery.service.match.alarm.application.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

  private final AlarmService alarmService;
}
