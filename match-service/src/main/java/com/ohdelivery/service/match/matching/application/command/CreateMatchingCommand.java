package com.ohdelivery.service.match.matching.application.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdelivery.common.feign.response.ClientGetRiderResponse;
import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.common.passport.usercontext.UserContextHolder;
import com.ohdelivery.service.match.common.command.MatchingCommand;
import com.ohdelivery.service.match.common.feign.RiderClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class CreateMatchingCommand implements MatchingCommand<Matching> {

  private final CreateMatchingRequest request;
  private final MatchingRepository matchingRepository;
  private final RiderClientService riderService;
  private final MatchingEventPublisher matchingEventPublisher;
  private UUID matchingId;
  private UUID deliveryId;

  @Override
  @Transactional
  public Matching execute() {
    deliveryId = request.getDeliveryId();

    // 중복 매칭 체크
    if (matchingRepository.findByDeliveryId(deliveryId).isPresent()) {
      throw new IllegalArgumentException("해당 배달에 대한 매칭이 이미 존재합니다.");
    }

    // 매칭 생성
    Matching matching = Matching.create(deliveryId);
    matchingRepository.save(matching);

    matchingId = matching.getId();

    Passport passport = UserContextHolder.getPassport();
    String passportJson;
    try {
      passportJson = new ObjectMapper().writeValueAsString(passport);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize passport", e);
    }
    // feign - 주변 라이더 조회
    List<ClientGetRiderResponse> nearbyRiders = riderService.getAllNearRider(
        passportJson,
        request.getStoreLatitude(),
        request.getStoreLongitude()
    ).getData();

    // 이벤트 발행: 매칭 생성 완료 -> 알람 해당 라이더들에게 보낼때 소비
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
    log.info("매칭 생성 완료: {}", matching.getId());
    return matching;
  }

  @Override
  @Transactional
  public void undo() {
    deliveryId = request.getDeliveryId();
    if (matchingId == null) { // execute() 실행안하고 undo 호출 || save 전에 에러 났을때 deliveryId가 null

      // 실패 시 이벤트 발행 -> 배달 삭제할때 소비됨
      matchingEventPublisher.matchingCreateFailedEvent(deliveryId);
      log.info("매칭 생성 취소(Undo): deliveryId={}", deliveryId);
      return;
    }

    matchingRepository.findByDeliveryId(deliveryId).ifPresent(matching -> {
      matching.cancel();
      matchingRepository.save(matching);

      matchingEventPublisher.matchingCreateFailedEvent(deliveryId);

      log.info("매칭 생성 취소(Undo): matchingId={}", matching.getId());
    });
  }

  private List<String> getRidersSlackIds(List<ClientGetRiderResponse> riders) {
    return riders.stream()
        .map(ClientGetRiderResponse::getSlack_id)
        .toList();
  }

  private List<Long> getRidersIds(List<ClientGetRiderResponse> riders) {
    return riders.stream()
        .map(ClientGetRiderResponse::getRider_id)
        .toList();
  }
}
