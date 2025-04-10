package com.ohdelivery.service.consult.agent.presentation;

import com.ohdelivery.service.consult.agent.application.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgentController {

  private final AgentService agentService;
}
