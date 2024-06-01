//package com.example.chat.config;
//
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//public class RedisPool {
//    private static JedisPool jedisPool;
//
//    @Value("${redis.host}")
//    private String host;
//
//    @Value("${redis.port}")
//    private int port;
//
//    @PostConstruct
//    private void initialize(){
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(50); // 最大连接数
//        poolConfig.setMaxIdle(10); // 最大空闲连接数
//        poolConfig.setMinIdle(5); // 最小空闲连接数
//        poolConfig.setTestOnBorrow(true); // 在借用连接时测试其有效性
//        poolConfig.setTestOnReturn(true); // 在返回连接时测试其有效性
//        poolConfig.setBlockWhenExhausted(true); // 连接耗尽时是否阻塞，false 抛出异常
//
//        jedisPool = new JedisPool(poolConfig, host, port);
//    }
//
//    public static JedisPool getPool(){
//        return jedisPool;
//    }
//
//
//}
