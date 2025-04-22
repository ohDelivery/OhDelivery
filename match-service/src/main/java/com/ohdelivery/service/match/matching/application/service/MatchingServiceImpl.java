package com.ohdelivery.service.match.matching.application.service;

import com.ohdelivery.common.feign.GetDeliveryResponse;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.service.match.common.command.CommandInvoker;
import com.ohdelivery.service.match.common.feign.DeliveryClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.command.CreateMatchingCommand;
import com.ohdelivery.service.match.matching.application.command.MatchingCommandFactory;
import com.ohdelivery.service.match.matching.application.command.UpdateMatchingCommand;
import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.dto.response.SearchMatchingResponse;
import com.ohdelivery.service.match.matching.application.exception.MatchingCreateException;
import com.ohdelivery.service.match.matching.application.exception.MatchingNotFoundException;
import com.ohdelivery.service.match.matching.application.exception.MatchingUpdateException;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import com.ohdelivery.service.match.rider.application.service.RiderService;
import com.ohdelivery.service.match.rider.domain.model.Rider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

  private final MatchingRepository matchingRepository;
  private final MatchingEventPublisher matchingEventPublisher;
  private final RiderService riderService;
  private final DeliveryClientService deliveryService;
  private final RedissonClient redissonClient;

  private final MatchingCommandFactory matchingCommandFactory;
  private final CommandInvoker commandInvoker;

  @Override
  @Transactional
  public UUID createMatching(CreateMatchingRequest request) {
    try {
      CreateMatchingCommand command = matchingCommandFactory.createMatchingCommand(request);
      Matching matching = commandInvoker.invoke(command);
      log.info("매칭 생성 성공: 배달Id:{}, 매칭Id{}", matching.getDeliveryId(), matching.getId());
      return matching.getId();
    } catch (Exception e) {
      log.error("매칭 생성 실패: {}", e.getMessage());
      throw new MatchingCreateException("매칭 생성 중 오류 발생: " + e.getMessage());
    }
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
    try {
      UpdateMatchingCommand command = matchingCommandFactory.updateMatchingCommand(matchingId,
          request, currentUser);
      commandInvoker.invoke(command);
    } catch (Exception e) {
      log.error("매칭 수정 실패: {}", e.getMessage());
      throw new MatchingUpdateException("매칭 수정 중 오류 발생: " + e.getMessage());
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
      UUID riderId = riderService.getRiderByUserId(userId).getId();
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
