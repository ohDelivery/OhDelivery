package com.ohdelivery.service.user.application;

public interface UserEventPublisher {
    void produceCreateUser(Long userId);
    void produceUpdateSlackId(Long userId, String slackId);
    void produceDeleteUser(Long userId);
}
