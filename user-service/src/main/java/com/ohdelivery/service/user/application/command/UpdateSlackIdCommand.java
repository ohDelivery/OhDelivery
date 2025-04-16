package com.ohdelivery.service.user.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateSlackIdCommand {
    Long id;
    String slackId;

    public static UpdateSlackIdCommand create(Long id, String slackId) {
        return new UpdateSlackIdCommand(id, slackId);
    }
}
