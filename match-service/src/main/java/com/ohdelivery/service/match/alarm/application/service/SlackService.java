package com.ohdelivery.service.match.alarm.application.service;

import com.ohdelivery.service.match.alarm.application.exception.AlarmErrorCode;
import com.ohdelivery.service.match.alarm.application.exception.AlarmException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackService {

  @Value(value = "${slack.token}")
  private String slackToken;
  private final Slack slack = Slack.getInstance();

  public void sendSlackMessage(String slackEmail, String message)
      throws IOException, SlackApiException {
    // 사용자 slack ID 조회
    String slackId = getSlackUserIdByEmail(slackEmail);

    // 사용자와 DM 채널 생성
    String dmChannelId = getDmChannelId(slackId);

    // DM 채널 ID를 사용해 메시지 전송
    ChatPostMessageResponse response = createChatPostMessage(dmChannelId, message);

    // response 검증
    validateSlackResponse(response);
  }

  // Slack 사용자 이메일로 사용자 ID 조회
  private String getSlackUserIdByEmail(String email) throws IOException, SlackApiException {
    UsersLookupByEmailResponse response = slack.methods(slackToken).usersLookupByEmail(
        UsersLookupByEmailRequest.builder().email(email).build()
    );

    return validateSlackUserIdResponse(response);
  }

  // Slack 사용자 ID로 DM 채널 생성 및 조회
  private String getDmChannelId(String receiverId) throws IOException, SlackApiException {
    ConversationsOpenResponse response = slack.methods(slackToken).conversationsOpen(
        ConversationsOpenRequest.builder().users(List.of(receiverId)).build()
    );
    validateSlackResponse(response);
    return response.getChannel().getId();
  }

  // Slack DM 메시지 전송
  private ChatPostMessageResponse createChatPostMessage(String dmChannelId, String text)
      throws IOException, SlackApiException {
    return slack.methods(slackToken).chatPostMessage(
        ChatPostMessageRequest.builder()
            .channel(dmChannelId)
            .text(text)
            .build()
    );
  }

  // Slack 사용자 ID 응답 검증
  private String validateSlackUserIdResponse(UsersLookupByEmailResponse response) {
    if (response.isOk() && response.getUser() != null) {
      return response.getUser().getId();
    } else {
      throw new AlarmException(AlarmErrorCode.SLACK_USER_ID_NOT_FOUND);
    }
  }

  // slack 응답 검증
  private <T> void validateSlackResponse(T response) {
    if (response instanceof ChatPostMessageResponse
        && !((ChatPostMessageResponse) response).isOk()) {
      throw new AlarmException(AlarmErrorCode.SLACK_MESSAGE_SEND_FAILED);
    } else if (response instanceof ConversationsOpenResponse
        && !((ConversationsOpenResponse) response).isOk()) {
      throw new AlarmException(AlarmErrorCode.SLACK_CHANNEL_OPEN_FAILED);
    }
  }
}
