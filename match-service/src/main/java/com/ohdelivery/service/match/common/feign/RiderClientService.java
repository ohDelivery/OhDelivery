package com.ohdelivery.service.match.common.feign;

import com.ohdelivery.common.feign.request.ClientGetNearRidersRequest;
import com.ohdelivery.common.feign.response.ClientGetRiderResponse;
import com.ohdelivery.common.response.ApiResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "match-service")
public interface RiderClientService {

  @GetMapping("/api/riders/nearRiders")
  ApiResponse<List<ClientGetRiderResponse>> getAllNearRider(
      @RequestBody ClientGetNearRidersRequest request);


  @GetMapping("/api/riders/user/{userId}")
  ApiResponse<ClientGetRiderResponse> getRiderByUserId(@PathVariable("userId") long riderId);
}
