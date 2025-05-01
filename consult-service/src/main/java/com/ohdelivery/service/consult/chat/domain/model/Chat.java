package com.ohdelivery.service.consult.chat.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_chatroom")
public class Chat {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String agentId;
  private String riderId;
  private LocalDateTime chatStartTime;
  private LocalDateTime chatEndTime;

  public static Chat create(String agentId, String riderId) {
    return Chat.builder()
        .agentId(agentId)
        .riderId(riderId)
        .chatStartTime(LocalDateTime.now())
        .build();
  }

  public void updateChatEndTime(LocalDateTime endTime) {
    this.chatEndTime = endTime;
  }
}
