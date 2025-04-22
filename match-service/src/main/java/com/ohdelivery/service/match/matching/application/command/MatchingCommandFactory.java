package com.ohdelivery.service.match.matching.application.command;

import com.ohdelivery.service.match.common.command.CommandFactory;
import com.ohdelivery.service.match.common.feign.RiderClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingCommandFactory implements CommandFactory {

  private final MatchingRepository matchingRepository;
  private final RiderClientService riderService;
  private final MatchingEventPublisher matchingEventPublisher;

  public CreateMatchingCommand createMatchingCommand(CreateMatchingRequest request) {
    return new CreateMatchingCommand(request, matchingRepository, riderService,
        matchingEventPublisher);
  }
}
