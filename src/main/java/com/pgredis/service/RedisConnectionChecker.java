package com.pgredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisConnectionChecker {

    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisConnectionChecker(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public boolean isRedisConnected() {
        try {
            redisConnectionFactory.getConnection();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


