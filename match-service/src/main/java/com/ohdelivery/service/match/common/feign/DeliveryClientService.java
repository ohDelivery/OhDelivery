package com.ohdelivery.service.match.common.feign;

import com.ohdelivery.common.feign.GetDeliveryResponse;
import com.ohdelivery.common.response.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "delivery-service")
public interface DeliveryClientService {

  @GetMapping("/deliveries/{deliveryId}")
  ApiResponse<GetDeliveryResponse> getDelivery(@PathVariable UUID deliveryId);
}
