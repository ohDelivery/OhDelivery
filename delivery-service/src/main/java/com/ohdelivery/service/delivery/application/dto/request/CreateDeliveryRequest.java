package com.ohdelivery.service.delivery.application.dto.request;

import com.ohdelivery.common.annotations.ValidEnum;
import com.ohdelivery.service.delivery.domain.model.Delivery;
import com.ohdelivery.service.delivery.domain.model.DeliveryStatus;
import com.ohdelivery.service.delivery.domain.model.OrderInfo;
import com.ohdelivery.service.delivery.domain.model.PathInfo;
import com.ohdelivery.service.delivery.domain.model.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateDeliveryRequest {

    @NotBlank
    private String storeName;
    @NotBlank
    private String storeAddress;
    @NotBlank
    private String orderDetails;
    @NotBlank
    private String orderRequest;
    @NotNull
    private Integer fee;
    @ValidEnum(enumClass = PaymentType.class)
    private PaymentType paymentType;
    @NotNull
    private Integer paymentAmount;
    @NotBlank
    private String targetAddress;

    public Delivery toDelivery(Integer expectedTime, Integer shortedDistance) {
        return Delivery.builder()
            .orderInfo(new OrderInfo(this.storeName, this.storeAddress, this.orderDetails,
                this.orderRequest))
            .targetAddress(this.targetAddress)
            .pathInfo(new PathInfo(expectedTime, shortedDistance))
            .fee(this.fee)
            .status(DeliveryStatus.WAITING_FOR_RECEPTION)
            .paymentType(this.paymentType)
            .paymentAmount(this.paymentAmount)
            .build();
    }
}
