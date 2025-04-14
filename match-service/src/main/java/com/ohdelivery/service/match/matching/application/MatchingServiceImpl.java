package com.ohdelivery.service.match.matching.application;

import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.exception.MatchingNotFoundException;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

  private final MatchingRepository matchingRepository;
  private final MatchingEventPublisher matchingEventPublisher;
  private final RiderService riderService;

  @Override
  @Transactional
  public UUID createMatching(CreateMatchingRequest request) {
    UUID deliveryId = request.getDeliveryId();

    Matching matching = Matching.create(deliveryId);

    matchingRepository.save(matching);
//    TODO : rider에서 후보 라이더들 받아오기, 좌표 정보들로 받아오게 고치기
    List<String> slackIdList = riderService.getRidersByLocation(request.getStoreAddress());

    matchingEventPublisher.matchingCreatedEvent(
        slackIdList,
        matching.getId(),
        request.getFee(),
        request.getStoreName(),
        request.getTargetAddress(),
        request.getOrderRequest()
    );
    return matching.getId();
  }

  @Override
  public GetMatchingResponse getMatching(UUID matchingId) {
    Matching matching = matchingRepository.findById(matchingId)
        .orElseThrow(() -> new MatchingNotFoundException());

    UUID deliveryId = matching.getDeliveryId();
//    TODO : 배달엔티티에서 라이더에게 보여줄 내용 가져오기(feign client)

    return new GetMatchingResponse(matching.getId(), matching.getRiderId(),
        matching.getDeliveryId());
  }

  @Override
  @Transactional
  public void updateMatching(UUID matchingId, AssignRiderRequest request) {
    Matching matching = matchingRepository.findById(matchingId)
        .orElseThrow(() -> new MatchingNotFoundException());

    if (!matching.isUpdatable()) {
      throw new IllegalArgumentException("Matching is not updatable");
    }

    UUID riderId = request.getRiderId();
    if (!riderService.checkAssignAvailable(riderId)) {
      throw new IllegalArgumentException("Rider is not assignable");
    }

    matching.assignRider(riderId);
    matchingRepository.save(matching);

    UUID deliveryID = matching.getDeliveryId();
    matchingEventPublisher.matchingCompletedEvent(deliveryID, riderId);
  }


  @Override
  public void deleteMatching(UUID deliveryId) {
//    배달 취소 기능 -> 배달 취소 시 매칭도 없어져야 하는부분
    Matching matching = matchingRepository.findByDeliveryId(deliveryId)
        .orElseThrow(() -> new MatchingNotFoundException());

    if (matching.isUpdatable()) {
      LocalDateTime now = LocalDateTime.now();
      String createdBy = "system";
      matching.delete(now, createdBy);
      matchingRepository.save(matching);
    } else {
      throw new IllegalArgumentException("Matching is not updatable");
    }
  }

  public List<Matching> getMatchings(UUID riderId) {
    if (riderId == null) {
      throw new IllegalArgumentException("RiderId is null");
    }

    return matchingRepository.findByRiderId(riderId);
  }
}
