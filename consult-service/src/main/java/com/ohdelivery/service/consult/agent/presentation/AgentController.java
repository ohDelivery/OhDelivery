package com.ohdelivery.service.consult.agent.presentation;

import com.ohdelivery.common.passport.RoleCheck;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.consult.agent.application.dto.request.CreateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.request.UpdateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.response.AgentResponse;
import com.ohdelivery.service.consult.agent.application.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consults/agents")
@RequiredArgsConstructor
public class AgentController {

  private final AgentService agentService;

  @RoleCheck(RoleType.MASTER)
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

  @RoleCheck({RoleType.MASTER, RoleType.AGENT})
  @PatchMapping("/{agentId}")
  public ResponseEntity<ApiResponse<AgentResponse>> updateAgent(
      @PathVariable Long agentId,
      @RequestBody UpdateAgentRequest request) {
    AgentResponse response = agentService.updateAgent(agentId, request);

    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @GetMapping("/{agentId}")
  public ResponseEntity<ApiResponse<AgentResponse>> getAgent(@PathVariable Long agentId) {
    AgentResponse response = agentService.getAgent(agentId);

    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @RoleCheck({RoleType.MASTER, RoleType.AGENT})
  @DeleteMapping("/{agentId}")
  public ResponseEntity<ApiResponse<Void>> deleteAgent(@PathVariable Long agentId) {
    agentService.deleteAgent(agentId);

    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }
}
