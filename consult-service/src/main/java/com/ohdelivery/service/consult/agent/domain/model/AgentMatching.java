package com.ohdelivery.service.consult.agent.domain.model;

import com.ohdelivery.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_agent_matching")
public class AgentMatching extends BaseEntity {

  @Id
  @UuidGenerator
  private UUID id;

  private Long agentId;

  private Long riderId;

  private UUID chatId;
}
