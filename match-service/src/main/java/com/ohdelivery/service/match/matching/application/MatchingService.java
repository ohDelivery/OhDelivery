package com.ohdelivery.service.match.matching.application;

import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.dto.request.UpdateMatchingRequest;
import java.util.UUID;

public interface MatchingService {
    UUID createMatching(CreateMatchingRequest request);
    GetMatchingResponse getMatching(UUID id);
    void updateMatching(UUID id, UpdateMatchingRequest request);
    void deleteMatching(UUID id);
}
