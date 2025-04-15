package com.ohdelivery.service.delivery.application.dto.response;

import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryStatus;
import com.ohdelivery.service.delivery.domain.model.PaymentType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryResponse {

    private final UUID deliveryId;
    private final String storeName;
    private final String storeAddress;
    private final String orderDetails;
    private final String orderRequest;
    private final String targetAddress;
    private final Integer expectedTime;
    private final Integer shortedDistance;
    private final Integer fee;
    private final DeliveryStatus status;
    private final PaymentType paymentType;
    private final Integer paymentAmount;

    public static DeliveryResponse from(Delivery delivery) {
        return DeliveryResponse.builder()
            .deliveryId(delivery.getId())
            .storeName(delivery.getOrderInfo().getStoreName())
            .storeAddress(delivery.getOrderInfo().getStoreAddress())
            .orderDetails(delivery.getOrderInfo().getOrderDetails())
            .orderRequest(delivery.getOrderInfo().getOrderRequest())
            .targetAddress(delivery.getTargetAddress())
            .expectedTime(delivery.getPathInfo().getExpectedTime())
            .shortedDistance(delivery.getPathInfo().getShortedDistance())
            .fee(delivery.getFee())
            .status(delivery.getStatus())
            .paymentType(delivery.getPaymentType())
            .paymentAmount(delivery.getPaymentAmount())
            .build();
    }
}
