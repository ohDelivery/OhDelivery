package com.ohdelivery.service.consult.chat.infrastructure.persistence;

import com.ohdelivery.service.consult.chat.domain.model.Chat;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, UUID> {
  Optional<Chat> findById(UUID chatId);

}
