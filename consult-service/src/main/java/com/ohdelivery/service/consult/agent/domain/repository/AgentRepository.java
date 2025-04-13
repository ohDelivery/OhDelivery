package com.ohdelivery.service.consult.agent.domain.repository;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, UUID> {

  Optional<Agent> findByAgentIdAndDeletedAtIsNull(Long agentId);

  List<Agent> findByDeletedAtIsNull();

  List<Agent> findByStatus(AgentStatus agentStatus);
}
