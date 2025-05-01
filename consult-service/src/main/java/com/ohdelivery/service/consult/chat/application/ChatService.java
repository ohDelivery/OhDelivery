package com.ohdelivery.service.consult.chat.application;

import com.ohdelivery.service.consult.chat.domain.KafkaConstants;
import com.ohdelivery.service.consult.chat.domain.model.Chat;
import com.ohdelivery.service.consult.chat.domain.model.ChatMessage;
import com.ohdelivery.service.consult.chat.domain.repository.ChatRepository;
import com.ohdelivery.service.consult.chat.infrastructure.persistence.mongo.MongoChatRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

  private final ChatRepository chatRepository;
  private final MongoChatRepository mongoChatRepository;
  private final Messenger messenger;


  public Chat createChatroom(String agentId, String riderId) {
    return chatRepository.save(Chat.create(agentId, riderId));
  }

  public Chat getChatRoom(UUID chatId) {
    return chatRepository.getChat(chatId);
  }

  public void sendMessage(UUID chatId, String sender, String message) {
    ChatMessage chatMessage = ChatMessage.create(chatId, sender, message);
    messenger.sendMessage(KafkaConstants.TOPIC_CHAT, ChatMessage.create(chatId, sender, message));
    mongoChatRepository.save(chatMessage);
  }

  public List<ChatMessage> getChatHistory(UUID chatId) {
    return mongoChatRepository.findByChatId(chatId);
  }
}
