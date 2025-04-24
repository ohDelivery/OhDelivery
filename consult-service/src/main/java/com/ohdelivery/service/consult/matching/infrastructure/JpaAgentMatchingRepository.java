package com.ohdelivery.service.consult.matching.infrastructure;

import com.ohdelivery.service.consult.matching.domain.model.AgentMatching;
import com.ohdelivery.service.consult.matching.domain.repository.AgentMatchingRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAgentMatchingRepository extends JpaRepository<AgentMatching, UUID>,
    AgentMatchingRepository {

}
