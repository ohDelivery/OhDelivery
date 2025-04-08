package com.ohdelivery.service.match.rider.presentation;

import com.ohdelivery.service.match.rider.application.dto.request.CreateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.request.UpdateRiderRequest;
import com.ohdelivery.service.match.rider.application.dto.response.GetRiderResponse;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
public class RiderController {
  private final RiderService riderService;

  @PostMapping
  public ResponseEntity<UUID> create(@RequestBody CreateRiderRequest request) {
    UUID id = riderService.createRider(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetRiderResponse> getRider(@PathVariable UUID id){
    GetRiderResponse response = riderService.getRider(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateMatching(
      @PathVariable UUID id,
      @RequestBody UpdateRiderRequest request
  ){
    riderService.updateRider(id,request);
    return ResponseEntity.noContent().build();
  }
}
