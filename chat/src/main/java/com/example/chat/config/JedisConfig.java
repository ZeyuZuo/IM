package com.example.chat.config;

import com.example.core.element.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {

    @Bean
    public Jedis jedis(){
        return new Jedis(Config.REDIS_HOST, Config.REDIS_PORT);
    }
}
