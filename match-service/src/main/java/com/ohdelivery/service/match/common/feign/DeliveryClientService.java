package com.ohdelivery.service.match.common.feign;

import com.ohdelivery.common.feign.GetDeliveryResponse;
import com.ohdelivery.common.response.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "delivery-service")
public interface DeliveryClientService {

  @GetMapping("/deliveries/{deliveryId}")
  ApiResponse<GetDeliveryResponse> getDelivery(
      @RequestHeader("X-User-Role") String passportJson,
      @PathVariable UUID deliveryId);
}
