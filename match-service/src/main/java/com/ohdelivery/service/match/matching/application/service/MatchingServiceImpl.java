package com.ohdelivery.service.match.matching.application.service;

import com.ohdelivery.common.feign.GetDeliveryResponse;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.service.match.common.DeliveryClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.dto.response.SearchMatchingResponse;
import com.ohdelivery.service.match.matching.application.exception.MatchingNotFoundException;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

  private final MatchingRepository matchingRepository;
  private final MatchingEventPublisher matchingEventPublisher;
  private final RiderService riderService;
  private final DeliveryClientService deliveryService;
  private final RedissonClient redissonClient;

  @Override
  @Transactional
  public UUID createMatching(CreateMatchingRequest request) {
    UUID deliveryId = request.getDeliveryId();

    // 이미 존재하는 매칭 확인 후 예외 처리
    if (matchingRepository.findByDeliveryId(deliveryId).isPresent()) {
      throw new IllegalArgumentException("해당 배달에 대한 매칭이 이미 존재합니다.");
    }

    Matching matching = Matching.create(deliveryId);
    matchingRepository.save(matching);

    List<Rider> nearbyRiders;
    try {
      nearbyRiders = riderService.getRidersByLocation(
          request.getStoreLatitude(),
          request.getStoreLongitude());
    } catch (Exception e) {
      matchingEventPublisher.matchingCreateFailedEvent(deliveryId);
      throw new RuntimeException(e);
    }

    matchingEventPublisher.matchingCreatedEvent(
        getRidersSlackIds(nearbyRiders),
        getRidersIds(nearbyRiders),
        matching.getId(),
        request.getFee(),
        request.getStoreName(),
        request.getStoreAddress(),
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
    GetDeliveryResponse delivery = deliveryService.getDelivery(deliveryId).getData();
    return new GetMatchingResponse(matching.getId(), matching.getRiderId(),
        matching.getDeliveryId(), delivery);
  }

  @Override
  @Transactional
  public void updateMatching(UUID matchingId, AssignRiderRequest request, Passport currentUser) {
    // 매칭 ID를 기반으로 고유한 락 키 생성
    String lockKey = "matching:" + matchingId.toString();
    RLock lock = redissonClient.getLock(lockKey);
    RoleType role = currentUser.getRoleType();
    long userId = Long.parseLong(currentUser.getUserId());

    if (role != RoleType.MASTER) {
      if (userId != request.getRiderId()) {
        throw new IllegalArgumentException("MASTER가 아니면 본인만 할당 가능합니다.");
      }
    }

    try {
      // 락을 획득 시도 (최대 대기 시간: 5초, 락 유지 시간: 10초)
      boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);

      if (!isLocked) {
        throw new IllegalStateException("매칭에 대한 락 획득 실패: " + matchingId);
      }

      Matching matching = matchingRepository.findById(matchingId)
          .orElseThrow(() -> new MatchingNotFoundException());

      if (!matching.isUpdatable()) {
        throw new IllegalArgumentException("매칭은 수정할 수 없는 상태입니다.");
      }
      UUID riderId = riderService.getRiderByuserId(request.getRiderId()).getId();

      if (!riderService.checkAssignAvailable(riderId)) {
        throw new IllegalArgumentException("라이더는 할당 가능한 상태가 아닙니다.");
      }

      matching.assignRider(riderId);
      matchingRepository.save(matching);

      UUID deliveryID = matching.getDeliveryId();
      matchingEventPublisher.matchingCompletedEvent(deliveryID, riderId);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("락 획득 중 인터럽트 발생", e);
    } finally {
      // 항상 락 해제 (finally 블록에서 처리)
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
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
      throw new IllegalArgumentException("Matching is not deletable");
    }
  }

  @Override
  public Page<SearchMatchingResponse> searchMatchings(
      Passport currentUser,
      int page,
      int size,
      String sortBy,
      String direction
  ) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    long userId = Long.parseLong(currentUser.getUserId());

    if (currentUser.getRoleType() == RoleType.RIDER) {
      UUID riderId = riderService.getRiderByuserId(userId).getId();
      return matchingRepository.findByRiderId(riderId, pageable)
          .map(SearchMatchingResponse::from);
    }

    // 마스터라면 전체 매칭 조회
    return matchingRepository.findAll(pageable)
        .map(SearchMatchingResponse::from);
  }

  private List<String> getRidersSlackIds(List<Rider> riders) {
    return riders.stream()
        .map(Rider::getSlackId)
        .toList();
  }

  private List<Long> getRidersIds(List<Rider> riders) {
    return riders.stream()
        .map(Rider::getRiderId)
        .toList();
  }
}
