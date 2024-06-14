package com.qiu.apifinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutInSeconds));
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }





}
