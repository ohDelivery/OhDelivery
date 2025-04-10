package com.ohdelivery.service.consult.agent.application;

import com.ohdelivery.service.consult.agent.domain.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

  private final AgentRepository agentRepository;
}
