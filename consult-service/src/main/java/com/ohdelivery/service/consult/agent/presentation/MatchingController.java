package com.ohdelivery.service.consult.agent.presentation;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.consult.agent.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.consult.agent.application.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consults/matching")
@RequiredArgsConstructor
public class MatchingController {

  private final MatchingService matchingService;

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
