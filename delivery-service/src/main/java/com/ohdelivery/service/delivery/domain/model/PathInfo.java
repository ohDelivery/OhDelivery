package com.ohdelivery.service.delivery.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PathInfo {

    @Column(name = "expected_time", nullable = false)
    private Integer expectedTime;

    @Column(name = "shorted_distance", nullable = false)
    private Integer shortedDistance;
}
