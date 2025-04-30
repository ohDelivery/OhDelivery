package com.ohdelivery.service.consult.chat.infrastructure.persistence;

import com.ohdelivery.service.consult.chat.domain.model.Chat;
import com.ohdelivery.service.consult.chat.domain.repository.ChatRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRepositoryImpl implements ChatRepository {

  private final JpaChatRepository jpaChatRepository;

  @Override
  public Chat getChat(UUID chatId) {
    return jpaChatRepository.findById(chatId)
        .orElseThrow();//TODO make exception
  }

  @Override
  public Chat save(Chat chat) {
    return jpaChatRepository.save(chat);
  }
}
