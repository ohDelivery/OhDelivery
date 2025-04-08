package com.ohdelivery.service.delivery.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@Table(name = "p_delivery_record")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRecord {

    @Id
    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "rider_id", nullable = false)
    private UUID riderId;

    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "accepted_at", nullable = false)
    private LocalDateTime acceptedAt;

    @Column(name = "departed_at")
    private LocalDateTime departedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    public void complete() {
        this.deliveredAt = LocalDateTime.now();
    }

    public DeliveryRecord(UUID deliveryId, UUID riderId, Integer fee, LocalDateTime acceptedAt) {
        this.deliveryId = deliveryId;
        this.riderId = riderId;
        this.fee = fee;
        this.acceptedAt = acceptedAt;
    }
}
