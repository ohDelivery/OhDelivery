package com.ohdelivery.service.consult.matching.presentation;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.consult.matching.application.dto.CreateMatchingRequest;
import com.ohdelivery.service.consult.matching.application.service.AgentMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consults/matching")
@RequiredArgsConstructor
public class AgentMatchingController {

  private final AgentMatchingService matchingService;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createMatching(
      @RequestBody CreateMatchingRequest request) {
    matchingService.createMatching(request);

    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.CREATED_SUCCESS.getCode().toString(),
        SuccessCode.CREATED_SUCCESS.getMessage()
    ));
  }
}
