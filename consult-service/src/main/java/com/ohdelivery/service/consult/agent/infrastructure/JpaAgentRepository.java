package com.ohdelivery.service.consult.agent.infrastructure;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.repository.AgentRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAgentRepository extends JpaRepository<Agent, UUID>, AgentRepository {

}
