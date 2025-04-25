package com.ohdelivery.service.match.rider.presentation;

import com.ohdelivery.common.passport.RoleCheck;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderStatusRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.dto.response.UpdateRiderStatusResponse;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import com.ohdelivery.service.match.rider.presentation.resposne.RiderSuccessCode;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
public class RiderController {

  private final RiderService riderService;

  @RoleCheck(RoleType.MASTER)
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
  public ResponseEntity<ApiResponse<GetRiderResponse>> getRider(@PathVariable("id") UUID id) {
    GetRiderResponse response = riderService.getRider(id);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @GetMapping("/all")
  @RoleCheck(RoleType.MASTER)
  public ResponseEntity<ApiResponse<List<GetRiderResponse>>> getAllRider() {
    List<GetRiderResponse> response = riderService.getAllRider();
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @PatchMapping("/{id}/status")
  @RoleCheck({RoleType.MASTER, RoleType.RIDER})
  public ResponseEntity<ApiResponse<UpdateRiderStatusResponse>> updateRiderStatus(
      @PathVariable("id") UUID id,
      @RequestBody @Valid UpdateRiderStatusRequest request) {
    UpdateRiderStatusResponse response = riderService.updateRiderStatus(id, request);
    return ResponseEntity.ok(ApiResponse.success(
        RiderSuccessCode.RIDER_STATUS_CHANGE_SUCCESS.getCode().toString(),
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
  @RoleCheck(RoleType.MASTER)
  public ResponseEntity<ApiResponse<Void>> deleteMatching(@PathVariable UUID id) {
    riderService.deleteRider(id);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }

  @GetMapping("/nearRiders")
  public ResponseEntity<ApiResponse<List<GetRiderResponse>>> getAllNearRider(
      @RequestParam("sLat") double sLat,
      @RequestParam("sLon") double sLon) {

    List<GetRiderResponse> response = riderService.getRidersByLocation(sLat, sLon);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResponse<GetRiderResponse>> getRiderByUserId(
      @PathVariable("userId") long userId) {
    GetRiderResponse response = riderService.getRiderByUserId(userId);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }
}
