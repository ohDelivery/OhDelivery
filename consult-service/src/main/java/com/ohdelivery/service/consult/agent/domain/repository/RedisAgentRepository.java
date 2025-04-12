package com.ohdelivery.service.consult.agent.domain.repository;

import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
  private static final String RR_INDEX_KEY_PREFIX = "agent:rr:";      // 라운드 로빈 인덱스

  public void save(String agentId) {
    String initStatus = AgentStatus.OFFLINE.toString();

    // 상태 기반 상담원 리스트 조회용
    redisTemplate.opsForSet().add(AGENT_PREFIX + initStatus, agentId);

    // 특정 상담원 상태 조회용
    redisTemplate.opsForValue().set(AGENT_STATUS_PREFIX + agentId, initStatus);
  }

  public void updateStatus(String agentId, String status) {
    String key = AGENT_STATUS_PREFIX + agentId;
    String oldStatus = redisTemplate.opsForValue().get(key);

    redisTemplate.opsForSet().remove(AGENT_PREFIX + oldStatus, agentId); // 기존 상태 set에서 삭제
    redisTemplate.opsForSet().add(AGENT_PREFIX + status, agentId);       // 새로운 상태 set에 추가
    redisTemplate.opsForValue().set(AGENT_STATUS_PREFIX + key, status);       // 상담원 상태 업데이트
  }

  // 특정 상태의 상담원 리스트 조회
  public List<Long> findByStatus(String status) {
    Set<String> agents = redisTemplate.opsForSet().members(AGENT_PREFIX + status);
    if (agents == null) {
      return List.of();
    }
    return agents.stream().map(Long::valueOf).toList();
  }

  // 상태별 라운드 로빈 방식으로 상담원 선택
  public Optional<Long> selectAgent() {
    String status = AgentStatus.AVAILABLE.toString();
    String key = AGENT_PREFIX + status;
    String indexKey = RR_INDEX_KEY_PREFIX + status;

    Set<String> agents = redisTemplate.opsForSet().members(key);
    if (agents == null) {
      return Optional.empty();
    }

    // list로 변환하여 정렬
    List<String> sortedAgents = new ArrayList<>(agents);
    Collections.sort(sortedAgents);

    // 인덱스 조회 및 업데이트
    int index = 0;
    String indexValue = redisTemplate.opsForValue().get(indexKey);
    if (indexValue != null) {
      index = Integer.parseInt(indexValue);
      index = (index + 1) % sortedAgents.size();
    }
    redisTemplate.opsForValue().set(indexKey, String.valueOf(index));
    return Optional.of(Long.valueOf(sortedAgents.get(index)));
  }

  public void delete(String agentId) {
    String status = redisTemplate.opsForValue().get(AGENT_STATUS_PREFIX + agentId);
    redisTemplate.opsForSet().remove(AGENT_PREFIX + status, agentId);
    redisTemplate.delete(AGENT_STATUS_PREFIX + agentId);
  }
}
