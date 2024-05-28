package com.example.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisUtils {
    private Jedis jedis;

    @Autowired
    public RedisUtils(Jedis jedis) {
        this.jedis = jedis;
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public Object get(String key) {
        return jedis.get(key);
    }

    public void del(String key) {
        jedis.del(key);
    }


}
