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

    private Jedis jedis;

    @Autowired
    public RocketMQService(RocketMQTemplate rocketMQTemplate, Jedis jedis){
        this.rocketMQTemplate = rocketMQTemplate;
        this.jedis = jedis;
    }

    public boolean sendMsgToPerson(Content content){
        ChatMsg chatMsg = content.getChatMsg();
        String receiver = chatMsg.getReceiver();
        String sender = chatMsg.getSender();
        String contentStr = JsonUtils.obj2Json(content);
        Message message = MessageBuilder.withPayload(contentStr).build();
        String redisStr = sender+"Server";


        rocketMQTemplate.asyncSend("");
    }
}
