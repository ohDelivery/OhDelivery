package com.ohdelivery.service.match.common.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  @Value("${spring.data.redis.host}")
  String host;

  @Value("${spring.data.redis.port}")
  String port;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();

    String address = "redis://" + host + ":" + port;

    config.useSingleServer()
        .setAddress(address) // Redis 서버 주소 설정
        .setPassword("root"); // Redis 비밀번호
    return Redisson.create(config);
  }
}
