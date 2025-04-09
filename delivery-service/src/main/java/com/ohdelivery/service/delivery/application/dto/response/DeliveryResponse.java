package com.ohdelivery.service.delivery.application.dto.response;

import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryStatus;
import com.ohdelivery.service.delivery.domain.model.PaymentType;
import java.util.UUID;
import lombok.Getter;

@Getter
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

    public DeliveryResponse(Delivery delivery) {
        this.deliveryId = delivery.getId();
        this.storeName = delivery.getOrderInfo().getStoreName();
        this.storeAddress = delivery.getOrderInfo().getStoreAddress();
        this.orderDetails = delivery.getOrderInfo().getOrderDetails();
        this.orderRequest = delivery.getOrderInfo().getOrderRequest();
        this.targetAddress = delivery.getTargetAddress();
        this.expectedTime = delivery.getPathInfo().getExpectedTime();
        this.shortedDistance = delivery.getPathInfo().getShortedDistance();
        this.fee = delivery.getFee();
        this.status = delivery.getStatus();
        this.paymentType = delivery.getPaymentType();
        this.paymentAmount = delivery.getPaymentAmount();
    }
}
