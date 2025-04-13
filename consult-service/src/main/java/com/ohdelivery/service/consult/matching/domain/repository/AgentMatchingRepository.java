package com.ohdelivery.service.consult.matching.domain.repository;

import com.ohdelivery.service.consult.matching.domain.model.AgentMatching;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentMatchingRepository extends JpaRepository<AgentMatching, UUID> {

}
