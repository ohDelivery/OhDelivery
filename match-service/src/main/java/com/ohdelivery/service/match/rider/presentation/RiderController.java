package com.ohdelivery.service.match.rider.presentation;

import com.ohdelivery.common.passport.RoleCheck;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.GetNearRidersRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderStatusRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.dto.response.UpdateRiderStatusResponse;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import com.ohdelivery.service.match.rider.presentation.resposne.RiderSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
@Tag(name = "Rider", description = "라이더 관련 API")
public class RiderController {

  private final RiderService riderService;

  @RoleCheck(RoleType.MASTER)
  @PostMapping
  @Operation(summary = "라이더 생성하기")
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
  @Operation(summary = "라이더 조회하기")
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
  @Operation(summary = "모든 라이더 조회하기")
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
  @Operation(summary = "라이더 상태 변경하기")
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
  @Operation(summary = "라이더 정보 변경하기")
  public ResponseEntity<ApiResponse<Void>> updateRider(
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
  @Operation(summary = "라이더 삭제하기")
  public ResponseEntity<ApiResponse<Void>> deleteRider(@PathVariable UUID id) {
    riderService.deleteRider(id);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }

  @GetMapping("/nearRiders")
  @RoleCheck(RoleType.MASTER)
  @Operation(summary = "해당 좌표 근처 라이더들 전부 조회하기")
  public ResponseEntity<ApiResponse<List<GetRiderResponse>>> getAllNearRider(
      @RequestBody GetNearRidersRequest request) {
    List<GetRiderResponse> response = riderService.getRidersByLocation(request.getSLat(),
        request.getSLon());
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        response
    ));
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "유저 아이디로 라이더 조회하기")
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
