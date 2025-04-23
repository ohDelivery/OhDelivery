package com.ohdelivery.service.match.alarm.infrastructure.persistence;

import com.ohdelivery.service.match.alarm.domain.model.AlarmSlack;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmSlackRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAlarmSlackRepository extends JpaRepository<AlarmSlack, UUID>,
    AlarmSlackRepository {

}
