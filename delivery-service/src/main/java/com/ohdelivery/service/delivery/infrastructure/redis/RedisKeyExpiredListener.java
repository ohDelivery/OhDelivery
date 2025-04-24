package com.ohdelivery.service.delivery.infrastructure.redis;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    public RedisKeyExpiredListener(
        RedisMessageListenerContainer listenerContainer,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        super(listenerContainer);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageToStr = message.toString();

        if (messageToStr.startsWith(RedisKey.RIDER_LOCATION_TTL_KEY)) {
            String riderId = messageToStr.split(":")[3];
            applicationEventPublisher.publishEvent(new RedisGeoExpiredEvent(riderId));
        }

    }
}
