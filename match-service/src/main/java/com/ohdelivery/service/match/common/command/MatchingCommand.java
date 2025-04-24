package com.ohdelivery.service.match.common.command;

/*
  매칭쪽(match-service에 matching) 커맨드 패턴 적용 위한 command 인터페이스
 */
public interface MatchingCommand<T> {

  T execute();

  void undo();
}
