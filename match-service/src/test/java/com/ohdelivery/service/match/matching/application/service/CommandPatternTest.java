package com.ohdelivery.service.match.matching.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ohdelivery.common.feign.response.ClientGetRiderResponse;
import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.service.match.common.command.CommandInvoker;
import com.ohdelivery.service.match.common.feign.RiderClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.command.CreateMatchingCommand;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CommandPatternTest {

  @Mock
  private MatchingRepository matchingRepository;
  @Mock
  private RiderClientService riderService;
  @Mock
  private MatchingEventPublisher matchingEventPublisher;

  private CommandInvoker commandInvoker;
  private CreateMatchingRequest request;

  @BeforeEach
  void setUp() {
    commandInvoker = new CommandInvoker();

    request = CreateMatchingRequest.builder()
        .deliveryId(UUID.randomUUID())
        .storeName("Test Store")
        .storeAddress("123 Test Street")
        .storeLongitude(127.0)
        .storeLatitude(37.0)
        .orderDetails("치킨 1마리")
        .orderRequest("문 앞에 놔주세요")
        .targetAddress("456 Customer Road")
        .expectedTime(30)
        .shortedDistance(1000)
        .fee(3000)
        .build();
  }

  @Test
  @DisplayName("CreateMatchingCommand 실행 시 매칭이 생성되고 이벤트가 발행된다")
  void executeCommand_createsMatchingAndPublishesEvent() {
    // given
    UUID generatedId = UUID.randomUUID();
    when(matchingRepository.findByDeliveryId(any())).thenReturn(Optional.empty());
    when(matchingRepository.save(any())).thenAnswer(invocation -> {
      Matching arg = invocation.getArgument(0);
      ReflectionTestUtils.setField(arg, "id", generatedId);
      return arg;
    });

    // 주변 라이더 mock 데이터
    List<ClientGetRiderResponse> mockRiders = List.of(
        new ClientGetRiderResponse(
            UUID.randomUUID(),    // id
            1L,                   // rider_id
            "rider1_slack",       // slack_id
            "AVAILABLE",          // status
            37.123,               // latitude
            127.456               // longitude
        )
    );
    // ApiResponse로 감싸기
    ApiResponse<List<ClientGetRiderResponse>> mockResponse = ApiResponse.success("200", "OK",
        mockRiders);

    // when 설정 추가
    when(riderService.getAllNearRider(any())).thenReturn(mockResponse);

    CreateMatchingCommand command = new CreateMatchingCommand(
        request,
        matchingRepository,
        riderService,
        matchingEventPublisher
    );

    // when
    Matching result = commandInvoker.invoke(command);

    // then
    assertNotNull(result);
    verify(matchingRepository).save(any(Matching.class));
    verify(matchingEventPublisher).matchingCreatedEvent(
        anyList(), anyList(), any(UUID.class), anyInt(), anyString(), anyString(), anyString(),
        anyString()
    );
  }

  @Test
  @DisplayName("CreateMatchingCommand 실행 중 예외 발생 시 Undo가 호출되어 이벤트 발행된다")
  void invokeCommand_throwsException_triggersUndo() {
    // given
    when(matchingRepository.findByDeliveryId(any())).thenReturn(Optional.empty());
    when(matchingRepository.save(any())).thenThrow(new RuntimeException("DB 오류"));

    CreateMatchingCommand command = new CreateMatchingCommand(
        request,
        matchingRepository,
        riderService,
        matchingEventPublisher
    );

    // when & then
    assertThrows(RuntimeException.class, () -> commandInvoker.invoke(command));
    verify(matchingEventPublisher).matchingCreateFailedEvent(request.getDeliveryId());
  }
}