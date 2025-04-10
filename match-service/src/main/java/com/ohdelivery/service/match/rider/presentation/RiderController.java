package com.ohdelivery.service.match.rider.presentation;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
public class RiderController {
  private final RiderService riderService;

  @PostMapping
  public ResponseEntity<ApiResponse<UUID>> create(@RequestBody CreateRiderRequest request) {
    UUID id = riderService.createRider(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(
            SuccessCode.CREATED_SUCCESS.getCode().toString(),
            SuccessCode.CREATED_SUCCESS.getMessage(),
            id
        ));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GetRiderResponse>> getRider(@PathVariable UUID id) {
    GetRiderResponse response = riderService.getRider(id);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateMatching(
      @PathVariable UUID id,
      @RequestBody UpdateRiderRequest request
  ) {
    riderService.updateRider(id, request);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteMatching(@PathVariable UUID id) {
    riderService.deleteRider(id);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }
}
