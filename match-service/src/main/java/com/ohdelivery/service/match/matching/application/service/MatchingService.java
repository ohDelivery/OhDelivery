package com.ohdelivery.service.match.matching.application.service;

import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.dto.response.SearchMatchingResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface MatchingService {

  UUID createMatching(CreateMatchingRequest request);

  GetMatchingResponse getMatching(UUID id);

  void updateMatching(UUID id, AssignRiderRequest request, Passport currentUser);

  void deleteMatching(UUID id);

  Page<SearchMatchingResponse> searchMatchings(Passport currentUser, int page, int size,
      String sortBy,
      String direction);
}
