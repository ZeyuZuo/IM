package com.example.msg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {

    @Value("${redis.host}")
    private String redisUrl;

    @Value("${redis.port}")
    private Integer port;

    @Bean
    public Jedis jedis(){
        return new Jedis(redisUrl,port);
    }
}
