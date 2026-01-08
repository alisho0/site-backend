package com.dircomercio.site_backend.auth.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
    private static final int LIMIT = 10;
    private static final Duration WINDOW = Duration.ofSeconds(60);

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(String key) {
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(key, WINDOW);
        }

        return count <= LIMIT;
    }
}
