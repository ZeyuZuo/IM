package com.example.msg.service;

import com.alibaba.fastjson.JSONObject;
import com.example.core.element.ChatMsg;
import com.example.core.utils.JsonUtils;
import com.example.msg.mapper.jpa.GroupUserMapper;
import com.example.msg.mapper.po.GroupUserPo;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Objects;

@Service
public class MsgService {
    private final Jedis jedis;
    private RocketMQTemplate rocketMQTemplate;
    private GroupUserMapper groupUserMapper;

    @Autowired
    public MsgService(Jedis jedis,
                      RocketMQTemplate rocketMQTemplate,
                      GroupUserMapper groupUserMapper) {
        this.jedis = jedis;
        this.rocketMQTemplate = rocketMQTemplate;
        this.groupUserMapper = groupUserMapper;
    }

    private void sendMq(ChatMsg chatMsg, String ip) {
        String topic = ip.replace(".", "") + "netty";
        // 发送消息
        String message = JSONObject.toJSONString(chatMsg);
        rocketMQTemplate.asyncSendOrderly(topic, message, "0", new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("send success!");
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        System.out.println("send fail");
                    }
                }
        );
    }

    public void send2person(ChatMsg chatMsg) {
        String receiver = chatMsg.getReceiver();
        String ip = jedis.get(receiver + "Netty");
        if(ip == null) {
            // 没上线
            System.out.println(receiver + " offline");
            return;
        }else{
            sendMq(chatMsg, ip);
        }
    }

    public void send2group(ChatMsg chatMsg) {
        String group = chatMsg.getGroup();
        List<GroupUserPo> poList = groupUserMapper.findByGroup(group);
        for(GroupUserPo po : poList) {
            chatMsg.setReceiver(po.getUser());
            send2person(chatMsg);
        }
    }
}
