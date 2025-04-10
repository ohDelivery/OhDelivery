package com.ohdelivery.service.consult.agent.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_agent")
public class Agent extends BaseEntity {

  @Id
  @UuidGenerator
  private String id;

  private Long agent_id;

  private AgentStatus status;
}
