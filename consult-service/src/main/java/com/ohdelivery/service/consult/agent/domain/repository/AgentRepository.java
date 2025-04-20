package com.ohdelivery.service.consult.agent.domain.repository;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import java.util.Optional;

public interface AgentRepository {

  Agent save(Agent agent);

  Optional<Agent> findByAgentId(Long agentId);
}
