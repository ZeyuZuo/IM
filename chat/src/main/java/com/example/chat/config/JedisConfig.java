package com.example.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {
    @Value("192.168.171.141")
    private String redisUrl;

    @Value("6379")
    private Integer port;

    @Bean
    public Jedis jedis(){
        return new Jedis(redisUrl,port);
    }
}
