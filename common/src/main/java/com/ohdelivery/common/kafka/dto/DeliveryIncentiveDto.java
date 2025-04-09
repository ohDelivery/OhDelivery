package com.ohdelivery.common.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryIncentiveDto {
	private Integer expectedTime;//예상 소요 시간
	private Integer shortedDistance; //최단 거리
	private UUID riderId;
	private LocalDateTime departedAt; //배달 출발 시각
	private LocalDateTime deliveredAt; //배달 출발 시각

}
