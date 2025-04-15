package com.ohdelivery.service.delivery.infrastructure.config;


import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UUID> riderLocationTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, UUID> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(
            new Jackson2JsonRedisSerializer<>(UUID.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(UUID.class));

        return template;
    }
}
