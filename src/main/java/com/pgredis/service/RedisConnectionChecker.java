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

    public boolean isRedisUp() {
        try {

            redisConnectionFactory.getConnection().ping();
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}

