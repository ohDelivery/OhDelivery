package com.ohdelivery.service.delivery.domain.model;


import com.ohdelivery.common.model.BaseEntity;
import com.ohdelivery.service.delivery.application.exception.DeliveryInvalidStatusException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "p_delivery")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private OrderInfo orderInfo;

    @Column(name = "target_address", nullable = false)
    private String targetAddress;

    @Embedded
    private PathInfo pathInfo;

    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount;

    public void complete() {
        if (this.status != DeliveryStatus.DELIVERING) {
            throw new DeliveryInvalidStatusException("배달 중일 경우에만 배달 완료 처리를 할 수 있습니다!");
        }
        this.status = DeliveryStatus.DELIVERED;
    }


    public void updateFee(Integer fee) {
        if (this.status != DeliveryStatus.WAITING_FOR_RECEPTION) {
            throw new DeliveryInvalidStatusException("배달 기사가 배정 된 이후로는 배달료는 수정 할 수 없습니다!");
        }
        this.fee = fee;
    }
}
