package com.ohdelivery.service.delivery.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "p_delivery_record")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "departed_at", nullable = false)
    private LocalDateTime departedAt;

    @Column(name = "delivered_at", nullable = false)
    private LocalDateTime deliveredAt;

    public void complete() {
        this.deliveredAt = LocalDateTime.now();
    }
}
