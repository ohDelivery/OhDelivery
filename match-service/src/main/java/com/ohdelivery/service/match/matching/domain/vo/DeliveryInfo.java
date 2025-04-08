package com.ohdelivery.service.match.matching.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class DeliveryInfo {
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "target_address")
    private String destinationAddress;

    @Column(name = "item")
    private String deliveryItem;
}
