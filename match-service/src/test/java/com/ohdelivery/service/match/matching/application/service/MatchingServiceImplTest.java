package com.ohdelivery.service.match.matching.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ohdelivery.service.match.common.command.CommandInvoker;
import com.ohdelivery.service.match.matching.application.command.CreateMatchingCommand;
import com.ohdelivery.service.match.matching.application.command.MatchingCommandFactory;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.exception.MatchingCreateException;
import com.ohdelivery.service.match.matching.domain.Matching;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // lenient 설정
class MatchingServiceImplTest {

  @Mock
  private MatchingCommandFactory matchingCommandFactory;
  @Mock
  private CommandInvoker commandInvoker;

  @InjectMocks
  private MatchingServiceImpl matchingService;

  @Mock
  private CreateMatchingCommand createMatchingCommand;

  private CreateMatchingRequest request;
  private UUID deliveryId;
  private UUID matchingId;

  @BeforeEach
  void setUp() {
    deliveryId = UUID.randomUUID();
    matchingId = UUID.randomUUID();
    request = CreateMatchingRequest.builder()
        .deliveryId(UUID.randomUUID())
        .storeName("바른치킨 역삼점")
        .storeAddress("서울 강남구 테헤란로 123")
        .storeLongitude(127.031)
        .storeLatitude(37.498)
        .orderDetails("후라이드+양념")
        .orderRequest("문 앞에 놔주세요")
        .targetAddress("서울 강남구 삼성로 456")
        .expectedTime(30)
        .shortedDistance(1500)
        .fee(3000)
        .build();

    // 가짜 Matching 객체
    Matching matching = mock(Matching.class);
    when(matching.getId()).thenReturn(matchingId);
    when(matching.getDeliveryId()).thenReturn(deliveryId);

    // 커맨드 팩토리 -> 커맨드 생성
    when(matchingCommandFactory.createMatchingCommand(any())).thenReturn(createMatchingCommand);
    // 커맨드 인보커 -> 매칭 실행 결과 반환
    when(commandInvoker.invoke(any(CreateMatchingCommand.class))).thenReturn(matching);
  }

  @Test
  @DisplayName("매칭 생성이 성공하면 매칭 ID가 반환된다")
  void shouldReturnMatchingId_whenCreateMatchingSucceeds() {
    UUID result = matchingService.createMatching(request);

    assertNotNull(result);
    assertEquals(matchingId, result);
    verify(commandInvoker).invoke(createMatchingCommand);
  }

  @Test
  @DisplayName("매칭 생성 실패 시 MatchingCreateException이 발생한다")
  void shouldThrowMatchingCreateException_whenCommandExecutionFails() {
    when(commandInvoker.invoke(any(CreateMatchingCommand.class))).thenThrow(
        new RuntimeException("DB 오류"));

    MatchingCreateException exception = assertThrows(
        MatchingCreateException.class,
        () -> matchingService.createMatching(request)
    );

    assertTrue(exception.getMessage().contains("매칭 생성 중 오류 발생"));
  }
}
