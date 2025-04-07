package com.ohdelivery.service.match.matching.domain;

import com.ohdelivery.common.model.BaseEntity;
import com.ohdelivery.service.match.matching.domain.vo.DeliveryInfo;
import com.ohdelivery.service.match.matching.domain.vo.PayInfo;
import com.ohdelivery.service.match.matching.domain.vo.RiderInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_matching")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @Embedded
    private RiderInfo riderInfo;

    @Embedded
    private PayInfo payInfo;

    @Embedded
    private DeliveryInfo deliveryInfo;

    public Matching(RiderInfo riderInfo, PayInfo payInfo, DeliveryInfo deliveryInfo) {
        this.riderInfo = riderInfo;
        this.payInfo = payInfo;
        this.deliveryInfo = deliveryInfo;
    }
}
