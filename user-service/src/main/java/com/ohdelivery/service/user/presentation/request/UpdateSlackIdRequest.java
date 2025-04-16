package com.ohdelivery.service.user.presentation.request;

import com.ohdelivery.service.user.application.command.UpdateSlackIdCommand;
import lombok.Getter;

@Getter
public class UpdateSlackIdRequest {
    Long id;
    String slackId;

    public UpdateSlackIdCommand toCommand() {
        return UpdateSlackIdCommand.create(id, slackId);
    }
}
