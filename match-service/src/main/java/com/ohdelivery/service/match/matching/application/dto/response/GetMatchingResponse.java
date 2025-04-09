package com.ohdelivery.service.match.matching.application.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetMatchingResponse {
    private final UUID id;
    private final UUID riderId;
    private final UUID deliveryId;
    private final String storeName;
    private final String storeAddress;
    private final String destinationAddress;
    private final String deliveryItem;
    private final Integer assignedFee;
}
