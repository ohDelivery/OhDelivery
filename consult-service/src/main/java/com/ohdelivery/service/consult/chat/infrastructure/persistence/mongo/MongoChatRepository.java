package com.ohdelivery.service.consult.chat.infrastructure.persistence.mongo;

import com.ohdelivery.service.consult.chat.domain.model.ChatMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoChatRepository extends MongoRepository<ChatMessage, UUID> {
  List<ChatMessage> findByChatId(UUID chatId);
}
