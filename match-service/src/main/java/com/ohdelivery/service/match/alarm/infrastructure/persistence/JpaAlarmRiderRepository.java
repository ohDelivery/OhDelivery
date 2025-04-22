package com.ohdelivery.service.match.alarm.infrastructure.persistence;

import com.ohdelivery.service.match.alarm.domain.model.AlarmSlack;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRiderRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAlarmRiderRepository extends JpaRepository<AlarmSlack, UUID>,
    AlarmRiderRepository {

}
