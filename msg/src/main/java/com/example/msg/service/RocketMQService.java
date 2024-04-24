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

@Service
public class RocketMQService {
    private RocketMQTemplate rocketMQTemplate;
    private RedissonClient redissonClient;
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RocketMQService(RocketMQTemplate rocketMQTemplate, RedissonClient redissonClient){
        this.rocketMQTemplate = rocketMQTemplate;
        this.redissonClient = redissonClient;
    }

    public boolean sendMsgToPerson(Content content){
        ChatMsg chatMsg = content.getChatMsg();
        String receiver = chatMsg.getReceiver();
        String sender = chatMsg.getSender();
        String contentStr = JsonUtils.obj2Json(content);
        Message message = MessageBuilder.withPayload(contentStr).build();
        String redisStr = sender+"Server";

        try{
            if(!redisTemplate.hasKey(redisStr)){
                System.out.println("没有上线");
            }
            String nettyServer = redisTemplate.opsForValue().get(redisStr);
        }catch(Exception e){
            System.out.println("redis 出错");
        }

        rocketMQTemplate.asyncSend("");
    }
}
