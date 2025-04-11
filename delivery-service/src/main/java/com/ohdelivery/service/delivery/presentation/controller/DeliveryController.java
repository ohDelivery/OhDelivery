package com.ohdelivery.service.delivery.presentation.controller;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.dto.request.UpdateFeeRequest;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryRecordResponse;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryResponse;
import com.ohdelivery.service.delivery.application.service.DeliveryService;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
@Tag(name = "Delivery", description = "배달 관련 API")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // TODO RoleCheck 추가하기
    @PostMapping
    @Operation(summary = "배달 생성하기")
    public ResponseEntity<ApiResponse<DeliveryResponse>> createdDelivery(
        @RequestBody @Valid CreateDeliveryRequest request) {
        Delivery delivery = deliveryService.createDelivery(request);

        return ResponseEntity.ok(
            ApiResponse.success(
                SuccessCode.CREATED_SUCCESS.getCode().toString(),
                SuccessCode.CREATED_SUCCESS.getMessage(),
                new DeliveryResponse(delivery)
            )
        );
    }

    // TODO RoleCheck 추가하기
    @GetMapping("/{deliveryId}")
    @Operation(summary = "배달 조회하기")
    public ResponseEntity<ApiResponse<DeliveryResponse>> getDelivery(
        @PathVariable UUID deliveryId) {
        Delivery delivery = deliveryService.getDelivery(deliveryId);

        return ResponseEntity.ok(
            ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                SuccessCode.COMMON_SUCCESS.getMessage(),
                new DeliveryResponse(delivery)
            )
        );
    }

    // TODO RoleCheck 추가하기
    @PatchMapping("/{deliveryId}/complete")
    @Operation(summary = "배달 완료 처리하기")
    public ResponseEntity<ApiResponse<Void>> completeDelivery(
        @PathVariable UUID deliveryId) {
        deliveryService.completeDelivery(deliveryId);

        return ResponseEntity.ok(
            ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                SuccessCode.COMMON_SUCCESS.getMessage()
            )
        );
    }

    // TODO RoleCheck 추가하기
    @GetMapping("/record/{deliveryId}")
    @Operation(summary = "배달 기록 조회하기")
    public ResponseEntity<ApiResponse<DeliveryRecordResponse>> getDeliveryRecord(
        @PathVariable UUID deliveryId) {
        DeliveryRecord deliveryRecord = deliveryService.getDeliveryRecord(deliveryId);

        return ResponseEntity.ok(
            ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                SuccessCode.COMMON_SUCCESS.getMessage(),
                new DeliveryRecordResponse(deliveryRecord)
            )
        );
    }

    // TODO RoleCheck 추가하기
    @PatchMapping("/fee/{deliveryId}")
    @Operation(summary = "배달 요금 변경하기")
    public ResponseEntity<ApiResponse<Void>> updateFee(
        @PathVariable UUID deliveryId, @RequestBody @Valid UpdateFeeRequest request) {
        deliveryService.updateFee(deliveryId, request.getFee());

        return ResponseEntity.ok(
            ApiResponse.success(
                SuccessCode.COMMON_SUCCESS.getCode().toString(),
                SuccessCode.COMMON_SUCCESS.getMessage()
            )
        );
    }
}
