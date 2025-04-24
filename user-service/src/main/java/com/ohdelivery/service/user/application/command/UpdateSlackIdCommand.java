package com.ohdelivery.service.user.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateSlackIdCommand {
    String slackId;

    public static UpdateSlackIdCommand create(String slackId) {
        return new UpdateSlackIdCommand(slackId);
    }
}
