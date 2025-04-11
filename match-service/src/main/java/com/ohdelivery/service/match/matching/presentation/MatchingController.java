package com.ohdelivery.service.match.matching.presentation;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.match.matching.application.MatchingService;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.request.UpdateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matchings")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<ApiResponse<UUID>> createMatching(@RequestBody CreateMatchingRequest request) {
        UUID id = matchingService.createMatching(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                SuccessCode.CREATED_SUCCESS.getCode().toString(),
                SuccessCode.CREATED_SUCCESS.getMessage(),
                id
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetMatchingResponse>> getMatching(@PathVariable UUID id) {
        GetMatchingResponse response = matchingService.getMatching(id);
        return ResponseEntity.ok(ApiResponse.success(
            SuccessCode.COMMON_SUCCESS.getCode().toString(),
            SuccessCode.COMMON_SUCCESS.getMessage(),
            response
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateMatching(
        @PathVariable UUID id,
        @RequestBody UpdateMatchingRequest request) {
        matchingService.updateMatching(id, request);
        return ResponseEntity.ok(ApiResponse.success(
            SuccessCode.COMMON_SUCCESS.getCode().toString(),
            SuccessCode.COMMON_SUCCESS.getMessage()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMatching(@PathVariable UUID id) {
        matchingService.deleteMatching(id);
        return ResponseEntity.ok(ApiResponse.success(
            SuccessCode.COMMON_SUCCESS.getCode().toString(),
            SuccessCode.COMMON_SUCCESS.getMessage()
        ));
    }
}
