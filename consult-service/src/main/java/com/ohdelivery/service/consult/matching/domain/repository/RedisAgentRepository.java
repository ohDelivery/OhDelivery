package com.ohdelivery.service.consult.matching.domain.repository;

import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisAgentRepository {

  private final StringRedisTemplate redisTemplate;
  public static final String AGENT_PREFIX = "agent:";
  private static final String AGENT_STATUS_PREFIX = "agent:status:";

  private static final String RR_INDEX_PREFIX = "agent:rr:";      // 라운드 로빈 인덱스
  private static final String BUSY_COUNT_PREFIX = "agent:busy";   // 상담원 busy count

  public void save(String agentId) {
    String initStatus = AgentStatus.OFFLINE.toString();

    // 상태 기반 상담원 리스트 조회용
    redisTemplate.opsForSet().add(AGENT_PREFIX + initStatus, agentId);

    // 특정 상담원 상태 조회용
    redisTemplate.opsForValue().set(AGENT_STATUS_PREFIX + agentId, initStatus);
  }

  public String getStatus(String agentId) {
    return redisTemplate.opsForValue().get(AGENT_STATUS_PREFIX + agentId);
  }

  public void updateStatus(String agentId, String status) {
    String key = AGENT_STATUS_PREFIX + agentId;
    String oldStatus = redisTemplate.opsForValue().get(key);

    redisTemplate.opsForSet().remove(AGENT_PREFIX + oldStatus, agentId); // 기존 상태 set에서 삭제
    redisTemplate.opsForSet().add(AGENT_PREFIX + status, agentId);       // 새로운 상태 set에 추가
    redisTemplate.opsForValue().set(key, status);                             // 상담원 상태 업데이트
  }

  public Set<String> findByStatus(String status) {
    return redisTemplate.opsForSet().members(AGENT_PREFIX + status);
  }

  public Optional<String> getRRIndex() {
    String key = RR_INDEX_PREFIX + AgentStatus.AVAILABLE.toString();
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }

  public void updateRRIndex(String index) {
    String indexKey = RR_INDEX_PREFIX + AgentStatus.AVAILABLE.toString();
    redisTemplate.opsForValue().set(indexKey, String.valueOf(index));
  }

  public int getBusyCount(String agentId) {
    Double count = redisTemplate.opsForZSet().score(BUSY_COUNT_PREFIX, agentId);
    return count != null ? count.intValue() : 0;
  }

  public void increaseBusyCount(String agentId) {
    redisTemplate.opsForZSet().incrementScore(BUSY_COUNT_PREFIX, agentId, 1);
  }

  public void delete(String agentId) {
    String status = redisTemplate.opsForValue().get(AGENT_STATUS_PREFIX + agentId);
    redisTemplate.opsForSet().remove(AGENT_PREFIX + status, agentId);
    redisTemplate.delete(AGENT_STATUS_PREFIX + agentId);
  }
}
