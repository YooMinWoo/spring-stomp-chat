package com.example.spring_stomp_chat.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    // 데이터를 저장 (객체, TTL 설정 포함)
    public void setData(String key, Object value, Long expiredTime) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(expiredTime));
    }

    // 데이터를 조회
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 데이터 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
