package com.ohdelivery.service.consult.chat.domain.repository;

import com.ohdelivery.service.consult.chat.domain.model.Chat;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository {

  Chat getChat(UUID chatId);

  Chat save(Chat chat);
}
