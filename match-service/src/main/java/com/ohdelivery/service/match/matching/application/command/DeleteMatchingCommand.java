package com.ohdelivery.service.match.matching.application.command;

import com.ohdelivery.service.match.common.command.MatchingCommand;
import com.ohdelivery.service.match.matching.application.exception.MatchingNotFoundException;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class DeleteMatchingCommand implements MatchingCommand<Void> {

  private final UUID deliveryId;
  private final MatchingRepository matchingRepository;

  private Matching backupMatching; // undo를 위한 백업

  @Override
  @Transactional
  public Void execute() {
    Matching matching = matchingRepository.findByDeliveryId(deliveryId)
        .orElseThrow(MatchingNotFoundException::new);

    if (!matching.isUpdatable()) {
      throw new IllegalArgumentException("Matching is not deletable");
    }

    // Undo를 위해 백업
    backupMatching = matching.clone(); // 또는 deep copy. clone()은 도메인에서 구현 필요

    LocalDateTime now = LocalDateTime.now();
    String createdBy = "system";
    matching.delete(now, createdBy);

    matchingRepository.save(matching);
    log.info("매칭 삭제 완료: deliveryId={}, matchingId={}", deliveryId, matching.getId());
    return null;
  }

  @Override
  @Transactional
  public void undo() {
    if (backupMatching == null) {
      log.warn("Undo 실패: 백업된 매칭 정보 없음 (deliveryId={})", deliveryId);
      return;
    }

    // 백업한 상태로 복구
    matchingRepository.save(backupMatching);
    log.info("매칭 삭제 취소(Undo): 복원된 matchingId={}", backupMatching.getId());
  }
}
