package com.ohdelivery.service.consult.chat.domain.model;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatmesssages")
public class ChatMessage {
  @Id
  private String id;
  private UUID chatId;
  private String sender;
  private String message;
  private LocalDateTime messageTime;

  public static ChatMessage create(UUID chatId, String sender, String message) {
    return ChatMessage.builder()
        .chatId(chatId)
        .sender(sender)
        .message(message)
        .messageTime(java.time.LocalDateTime.now())
        .build();
  }
}
