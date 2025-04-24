package com.ohdelivery.service.match.matching.application.command;

import com.ohdelivery.common.passport.Passport;
import com.ohdelivery.service.match.common.command.CommandFactory;
import com.ohdelivery.service.match.common.feign.RiderClientService;
import com.ohdelivery.service.match.matching.application.MatchingEventPublisher;
import com.ohdelivery.service.match.matching.application.dto.request.AssignRiderRequest;
import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingCommandFactory implements CommandFactory {

  private final MatchingRepository matchingRepository;
  private final RiderClientService riderService;
  private final MatchingEventPublisher matchingEventPublisher;
  private final RedissonClient redissonClient;

  public CreateMatchingCommand createMatchingCommand(CreateMatchingRequest request) {
    return new CreateMatchingCommand(request, matchingRepository, riderService,
        matchingEventPublisher);
  }

  public UpdateMatchingCommand updateMatchingCommand(
      UUID matchingId,
      AssignRiderRequest request,
      Passport currentUser
  ) {
    return new UpdateMatchingCommand(
        matchingId,
        request,
        currentUser,
        matchingRepository,
        riderService,
        matchingEventPublisher,
        redissonClient
    );
  }

  public DeleteMatchingCommand deleteMatchingCommand(UUID deliveryId) {
    return new DeleteMatchingCommand(deliveryId, matchingRepository);
  }
}
