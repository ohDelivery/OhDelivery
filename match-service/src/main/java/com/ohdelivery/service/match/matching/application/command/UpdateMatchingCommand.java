package com.ohdelivery.service.match.matching.application.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.common.feign.response.ClientGetRiderResponse;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.common.passport.usercontext.UserContextHolder;
import com.ohdelivery.service.match.common.command.MatchingCommand;
import com.ohdelivery.service.match.common.feign.RiderClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.exception.MatchingNotFoundException;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class UpdateMatchingCommand implements MatchingCommand<Void> {

  private final UUID matchingId;
  private final AssignRiderRequest request;
  private final Passport currentUser;
  private final MatchingRepository matchingRepository;
  private final RiderClientService riderService;
  private final MatchingEventPublisher matchingEventPublisher;
  private final RedissonClient redissonClient;

  private UUID previousRiderId; // undo용

  @Override
  @Transactional
  public Void execute() {
    String lockKey = "matching:" + matchingId.toString();
    RLock lock = redissonClient.getLock(lockKey);
    RoleType role = currentUser.getRoleType();
    long userId = Long.parseLong(currentUser.getUserId());

    if (role != RoleType.MASTER && userId != request.getRiderId()) {
      throw new IllegalArgumentException("MASTER가 아니면 본인만 할당 가능합니다.");
    }

    try {
      boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
      if (!isLocked) {
        throw new IllegalStateException("매칭에 대한 락 획득 실패: " + matchingId);
      }

      Matching matching = matchingRepository.findById(matchingId)
          .orElseThrow(MatchingNotFoundException::new);

      if (!matching.isUpdatable()) {
        throw new IllegalArgumentException("매칭은 수정할 수 없는 상태입니다.");
      }

      Passport passport = UserContextHolder.getPassport();
      String passportJson;
      try {
        passportJson = new ObjectMapper().writeValueAsString(passport);
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Failed to serialize passport", e);
      }

      ClientGetRiderResponse rider = riderService.getRiderByUserId(passportJson,
          request.getRiderId()).getData();
      UUID riderId = rider.getId();
      boolean status = rider.isAvailable();
      if (!status) {
        throw new IllegalArgumentException("라이더는 할당 가능한 상태가 아닙니다.");
      }

      // undo를 위한 기존 값 저장
      previousRiderId = matching.getRiderId();

      matching.assignRider(riderId);
      matchingRepository.save(matching);

      matchingEventPublisher.matchingCompletedEvent(matching.getDeliveryId(), riderId);
      log.info("라이더 할당 완료: matchingId={}, riderId={}", matchingId, riderId);
      return null;

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("락 획득 중 인터럽트 발생", e);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  @Override
  public void undo() {
    try {
      Matching matching = matchingRepository.findById(matchingId)
          .orElseThrow(MatchingNotFoundException::new);

      // 이전 상태로 되돌리기 TODO : 물어보기
      if (previousRiderId != null) {
        matching.assignRider(previousRiderId); // 원래 있던 rider로 복원
      } else {
        matching.assignRider(null);
      }

      matchingRepository.save(matching);
      log.info("라이더 할당 취소(Undo): matchingId={}, 복원된 riderId={}", matchingId, previousRiderId);

    } catch (Exception e) {
      log.warn("라이더 할당 undo 실패: matchingId={}, error={}", matchingId, e.getMessage());
    }
  }
}
