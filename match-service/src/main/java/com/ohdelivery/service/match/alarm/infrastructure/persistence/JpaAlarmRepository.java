package com.ohdelivery.service.match.alarm.infrastructure.persistence;

import com.ohdelivery.service.match.alarm.domain.model.Alarm;
import com.ohdelivery.service.match.alarm.domain.repository.AlarmRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAlarmRepository extends JpaRepository<Alarm, UUID>, AlarmRepository {

}
