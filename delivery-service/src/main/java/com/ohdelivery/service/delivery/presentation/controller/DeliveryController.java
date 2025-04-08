package com.ohdelivery.service.delivery.presentation.controller;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.delivery.application.dto.request.CreateDeliveryRequest;
import com.ohdelivery.service.delivery.application.dto.response.DeliveryResponse;
import com.ohdelivery.service.delivery.application.service.DeliveryService;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<DeliveryResponse> getDelivery(
        @PathVariable UUID deliveryId) {
        Delivery delivery = deliveryService.getDelivery(deliveryId);

//        return ResponseEntity.ok(
//            ApiResponse.success(
//                SuccessCode.COMMON_SUCCESS.getCode().toString(),
//                SuccessCode.COMMON_SUCCESS.getMessage(),
//                new DeliveryResponse(delivery)
//            )
//        );
        return ResponseEntity.ok(new DeliveryResponse(delivery));
    }

    // TODO RoleCheck 추가하기
    @PutMapping("/{deliveryId}/complete")
    @Operation(summary = "배달 완료 처리하기")
    public ResponseEntity<Void> completeDelivery(
        @PathVariable UUID deliveryId) {
        deliveryService.completeDelivery(deliveryId);

        return ResponseEntity.noContent().build();
    }
}
