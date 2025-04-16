package com.ohdelivery.service.user.application;

public interface UserEventPublisher {
    void produceCreateUser(Long userId, String slackId);
    void produceUpdateSlackId(Long userId, String slackId);
    void produceDeleteUser(Long userId);
}
