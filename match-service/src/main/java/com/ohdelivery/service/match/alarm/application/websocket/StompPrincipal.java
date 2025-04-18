package com.ohdelivery.service.match.alarm.application.websocket;

import java.security.Principal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Stomp 메시지 전송을 위한 사용자 식별 객체
@Getter
@RequiredArgsConstructor
public class StompPrincipal implements Principal {

  // Spring에서 convertAndSendToUser() 호출 시 이 값으로 라우팅
  private final String name;

}
