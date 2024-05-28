package com.example.msg.service;

import com.example.core.element.ChatMsg;
import com.example.core.utils.JsonUtils;
import com.example.msg.mapper.jpa.GroupUserMapper;
import com.example.msg.mapper.po.GroupUserPo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class MsgService {
    private final Jedis jedis;
    private RocketMQTemplate rocketMQTemplate;
    private GroupUserMapper groupUserMapper;

    public MsgService(Jedis jedis,
                      RocketMQTemplate rocketMQTemplate,
                      GroupUserMapper groupUserMapper) {
        this.jedis = jedis;
        this.rocketMQTemplate = rocketMQTemplate;
        this.groupUserMapper = groupUserMapper;
    }

    private void sendMq(ChatMsg chatMsg, String ip) {
        String msg = JsonUtils.obj2Json(chatMsg);
        String topic = ip+"Netty";
        rocketMQTemplate.asyncSend();
    }

    public void send2person(ChatMsg chatMsg) {
        String receiver = chatMsg.getReceiver();
        String ip = jedis.get(receiver);
        if(ip == null) {
            // 没上线
            return;
        }else{
            sendMq(chatMsg, ip);
        }
    }

    public void send2group(ChatMsg chatMsg) {
        String group = chatMsg.getReceiver();
        List<GroupUserPo> poList = groupUserMapper.findByGroup(group);
        for(GroupUserPo po : poList) {
            chatMsg.setReceiver(po.getUser());
            send2person(chatMsg);
        }
    }
}
