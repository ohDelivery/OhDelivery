package com.ohdelivery.service.consult.agent.domain.repository;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, UUID> {

}
