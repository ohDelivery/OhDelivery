package com.ohdelivery.service.user.presentation.response;

import com.ohdelivery.service.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetUserResponse {
    private Long id;
    private String username;
    private String name;
    private String slackId;

    public static GetUserResponse create(Long id, String username, String name, String slackId) {
        return GetUserResponse.builder()
                .id(id)
                .username(username)
                .name(name)
                .slackId(slackId)
                .build();
    }
}
