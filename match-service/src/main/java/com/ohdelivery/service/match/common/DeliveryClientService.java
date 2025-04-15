package com.ohdelivery.service.match.common;

import com.ohdelivery.common.feign.GetDeliveryResponse;
import com.ohdelivery.common.response.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "delivery-service")
public interface DeliveryClientService {

  @GetMapping("/{deliveryId}")
  ApiResponse<GetDeliveryResponse> getDelivery(@PathVariable("deliveryId") UUID deliveryId);
}
