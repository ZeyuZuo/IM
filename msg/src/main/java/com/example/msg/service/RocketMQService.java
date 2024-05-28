package com.example.msg.service;

import com.example.core.element.ChatMsg;
import com.example.core.element.Content;
import com.example.core.utils.JsonUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RocketMQService {
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    public RocketMQService(RocketMQTemplate rocketMQTemplate, Jedis jedis){
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public boolean sendMsgToPerson(Content content){
        rocketMQTemplate.asyncSend("");
    }
}
