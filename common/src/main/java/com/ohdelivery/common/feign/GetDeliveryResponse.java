package com.ohdelivery.common.feign;

import java.util.UUID;
import lombok.Getter;

@Getter
public class GetDeliveryResponse {

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

  public GetDeliveryResponse(
      UUID deliveryId,
      String storeName,
      String storeAddress,
      String orderDetails,
      String orderRequest,
      String targetAddress,
      Integer expectedTime,
      Integer shortedDistance,
      Integer fee,
      DeliveryStatus status,
      PaymentType paymentType,
      Integer paymentAmount
  ) {
    this.deliveryId = deliveryId;
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.orderDetails = orderDetails;
    this.orderRequest = orderRequest;
    this.targetAddress = targetAddress;
    this.expectedTime = expectedTime;
    this.shortedDistance = shortedDistance;
    this.fee = fee;
    this.status = status;
    this.paymentType = paymentType;
    this.paymentAmount = paymentAmount;
  }

  public enum DeliveryStatus {
    READY, ASSIGNED, PICKED_UP, DELIVERED, CANCELLED
  }

  public enum PaymentType {
    CASH, CARD, ONLINE
  }
}
