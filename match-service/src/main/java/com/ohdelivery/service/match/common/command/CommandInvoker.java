package com.ohdelivery.service.match.common.command;

import java.util.ArrayDeque;
import java.util.Deque;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandInvoker implements AutoCloseable {

  private final ThreadLocal<Deque<MatchingCommand<?>>> commandHistoryHolder =
      ThreadLocal.withInitial(ArrayDeque::new);

  @Override
  public void close() {
    commandHistoryHolder.remove();
  }

  private final Deque<MatchingCommand<?>> executedCommands = new ArrayDeque<>();

  public <R> R invoke(MatchingCommand<R> command) {
    try {
      executedCommands.push(command);
      R result = command.execute();
      return result;
    } catch (Exception e) {
      rollback();
      throw e;
    }
  }

  private void rollback() {
    while (!executedCommands.isEmpty()) {
      MatchingCommand<?> command = executedCommands.pop();
      try {
        command.undo();
      } catch (Exception e) {
        log.warn("Undo failed for command {}: {}", command.getClass().getSimpleName(),
            e.getMessage(), e);
      }
    }
  }
}
