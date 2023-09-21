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
            // Tenta obter uma conexão com o Redis
            redisConnectionFactory.getConnection().ping();
            return true;
        } catch (Exception e) {
            // Lidar com erros de conexão
            e.printStackTrace();
            return false;
        }
    }
}

