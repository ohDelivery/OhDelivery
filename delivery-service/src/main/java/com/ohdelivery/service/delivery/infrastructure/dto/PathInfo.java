package com.ohdelivery.service.delivery.infrastructure.dto;

import com.ohdelivery.service.delivery.infrastructure.exception.InvalidAddressException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PathInfo {

    private Integer distance;
    private Integer duration;

    private List<List<Double>> path;

    public static PathInfo from(DirectionsResponse directionsResponse) {
        if (directionsResponse == null || directionsResponse.getCode() != 0) {
            throw new InvalidAddressException("경로를 찾을 수 없는 주소 입니다!");
        }

        Integer distance = directionsResponse.getDistance();
        Integer duration = directionsResponse.getDuration();
        List<List<Double>> path = directionsResponse.getRoute().getTraoptimal().get(0).getPath();

        return new PathInfo(distance, duration, path);
    }
}
