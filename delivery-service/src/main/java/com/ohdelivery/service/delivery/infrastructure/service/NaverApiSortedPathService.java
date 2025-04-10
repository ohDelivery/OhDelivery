package com.ohdelivery.service.delivery.infrastructure.service;

import com.ohdelivery.service.delivery.domain.service.ShortedPathService;
import com.ohdelivery.service.delivery.infrastructure.dto.DirectionsResponse;
import com.ohdelivery.service.delivery.infrastructure.dto.GeocodeResponse;
import com.ohdelivery.service.delivery.infrastructure.dto.LocationInfo;
import com.ohdelivery.service.delivery.infrastructure.dto.PathInfo;
import com.ohdelivery.service.delivery.infrastructure.exception.InvalidAddressException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverApiSortedPathService implements ShortedPathService {

    @Value("${naver.api-key.id}")
    private String keyId;
    @Value("${naver.api-key.secret}")
    private String keySecret;
    @Value("${naver.geocode.url}")
    private String geocodeUrl;
    @Value("${naver.directions.url}")
    private String directionsUrl;

    private static final String HEADER_KEY_ID = "x-ncp-apigw-api-key-id";
    private static final String HEADER_KEY = "x-ncp-apigw-api-key";

    private final WebClient webClient;

    @Override
    public LocationInfo getLocation(String address) {
        GeocodeResponse geocodeResponse = webClient.get()
            .uri(uriBuilder -> {
                try {
                    return new URIBuilder(geocodeUrl).addParameter("query", address).build();
                } catch (URISyntaxException e) {
                    log.error(e.getMessage());
                    throw new InvalidAddressException();
                }
            })
            .header(HEADER_KEY_ID, keyId)
            .header(HEADER_KEY, keySecret)
            .retrieve()
            .bodyToMono(GeocodeResponse.class)
            .block();

        return LocationInfo.fromGeocodeResponse(geocodeResponse);
    }

    @Override
    public PathInfo getPath(LocationInfo startLocation, LocationInfo goalLocation) {
        StringBuffer start = new StringBuffer()
            .append(startLocation.getLongitude())
            .append(",")
            .append(startLocation.getLatitude());

        StringBuffer goal = new StringBuffer()
            .append(goalLocation.getLongitude())
            .append(",")
            .append(goalLocation.getLatitude());

        DirectionsResponse directionsResponse = webClient.get()
            .uri(uriBuilder -> {
                try {
                    return new URIBuilder(directionsUrl)
                        .addParameter("start", start.toString())
                        .addParameter("goal", goal.toString())
                        .build();
                } catch (URISyntaxException e) {
                    log.error(e.getMessage());
                    throw new InvalidAddressException();
                }
            })
            .header(HEADER_KEY_ID, keyId)
            .header(HEADER_KEY, keySecret)
            .retrieve()
            .bodyToMono(DirectionsResponse.class)
            .block();

        return PathInfo.from(directionsResponse);
    }
}
