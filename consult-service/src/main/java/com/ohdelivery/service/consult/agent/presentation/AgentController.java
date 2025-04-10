package com.ohdelivery.service.consult.agent.presentation;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.consult.agent.application.dto.request.CreateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.response.AgentResponse;
import com.ohdelivery.service.consult.agent.application.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consults/agents")
@RequiredArgsConstructor
public class AgentController {

  private final AgentService agentService;

  @PostMapping
  public ResponseEntity<ApiResponse<AgentResponse>> createAgent(
      @RequestBody CreateAgentRequest request) {
    AgentResponse response = agentService.createAgent(request);

    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.CREATED_SUCCESS.getCode().toString(),
        SuccessCode.CREATED_SUCCESS.getMessage(),
        response
    ));
  }
}
