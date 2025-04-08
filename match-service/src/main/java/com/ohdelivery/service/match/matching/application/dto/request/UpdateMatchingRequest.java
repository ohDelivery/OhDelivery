package com.ohdelivery.service.match.matching.application.dto.request;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UpdateMatchingRequest {
    private UUID riderId;
    private UUID deliveryId;
    private String storeName;
    private String storeAddress;
    private String destinationAddress;
    private String deliveryItem;
    private Integer assignedFee;
}
