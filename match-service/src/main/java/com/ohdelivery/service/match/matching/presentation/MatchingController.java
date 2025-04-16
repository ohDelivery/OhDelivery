package com.ohdelivery.service.match.matching.presentation;

import com.ohdelivery.common.annotations.CurrentUser;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.passport.RoleCheck;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.common.response.SuccessCode;
import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.dto.response.SearchMatchingResponse;
import com.ohdelivery.service.match.matching.application.service.MatchingService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matchings")
@RequiredArgsConstructor
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping
  @RoleCheck(RoleType.MASTER)
  public ResponseEntity<ApiResponse<UUID>> createMatching(
      @RequestBody CreateMatchingRequest request) {
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

  //  TODO : 로그인한 사람의 Id가져와서 그 Id로 조회하기
  @PutMapping("/{id}")
  @RoleCheck({RoleType.MASTER, RoleType.RIDER})
  public ResponseEntity<ApiResponse<Void>> updateMatching(
      @PathVariable UUID id,
      @CurrentUser Passport currentUser,
      @RequestBody AssignRiderRequest request) {
    matchingService.updateMatching(id, request, currentUser);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }

  @DeleteMapping("/{id}")
  @RoleCheck(RoleType.MASTER)
  public ResponseEntity<ApiResponse<Void>> deleteMatching(@PathVariable UUID id) {
    matchingService.deleteMatching(id);
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage()
    ));
  }


  @GetMapping
  @RoleCheck({RoleType.MASTER, RoleType.RIDER})
  public ResponseEntity<ApiResponse<Page<SearchMatchingResponse>>> searchMatchings(
      @CurrentUser Passport currentUser,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String direction
  ) {
    Page<SearchMatchingResponse> matchings = matchingService.searchMatchings(
        currentUser, page, size, sortBy, direction
    );
    return ResponseEntity.ok(ApiResponse.success(
        SuccessCode.COMMON_SUCCESS.getCode().toString(),
        SuccessCode.COMMON_SUCCESS.getMessage(),
        matchings
    ));
  }

}
