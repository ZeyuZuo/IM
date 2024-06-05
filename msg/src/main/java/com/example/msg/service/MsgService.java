package com.example.msg.service;

import com.alibaba.fastjson.JSONObject;
import com.example.core.element.ChatMsg;
import com.example.msg.dao.MsgDao;
import com.example.msg.dao.bo.Msg;
import com.example.msg.mapper.jpa.GroupUserMapper;
import com.example.msg.mapper.po.GroupUserPo;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class MsgService {
    private final Logger logger = LoggerFactory.getLogger(MsgService.class);
    private final JedisPool jedisPool;

    private RocketMQTemplate rocketMQTemplate;
    private MsgDao msgDao;
    private GroupUserMapper groupUserMapper;

    @Autowired
    public MsgService(RocketMQTemplate rocketMQTemplate,
                      MsgDao msgDao,
                      GroupUserMapper groupUserMapper, JedisPool jedisPool) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.msgDao = msgDao;
        this.groupUserMapper = groupUserMapper;
        this.jedisPool = jedisPool;
    }

    private void sendMq(ChatMsg chatMsg, String ip) {
        String topic = ip.replace(".", "") + "netty";
        // 发送消息
        String message = JSONObject.toJSONString(chatMsg);
        rocketMQTemplate.asyncSendOrderly(topic, message, "0", new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        logger.debug("send rocketMQ message: {}, topic: {} successfully!!!", message, topic);
                        System.out.println("send success!");
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        logger.error("send rocketMQ message: {}, topic: {} failed!!!", message, topic);
                        System.out.println("send fail");
                    }
                }
        );
    }

    public void send2person(ChatMsg chatMsg) {
        String receiver = chatMsg.getReceiver();
        String ip = null;
        try (Jedis jedis = jedisPool.getResource()) {
            ip = jedis.get(receiver + "Netty");
        } catch (Exception e) {
            // 处理异常，记录日志等
            throw new RuntimeException("Error retrieving data from Redis", e);
        }
        logger.debug("receiver: {}, ip: {}", receiver, ip);
        Msg msg = new Msg(chatMsg);
        if(ip == null) {
            // 没上线
            // logger.debug("receiver {} is offline", receiver);
            System.out.println("receiver " + receiver + " is offline");
        }else{
            msg.setSent(true);
            sendMq(chatMsg, ip);
        }
        System.out.println("send msg: " + msg);
        String topic = "storageHH";
        String message = JSONObject.toJSONString(msg);
        rocketMQTemplate.asyncSendOrderly(topic, message, "0", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.debug("send rocketMQ message: {}, topic: {} successfully!!!", message, topic);
                System.out.println("send success!");
            }
            @Override
            public void onException(Throwable throwable) {
                logger.error("send rocketMQ message: {}, topic: {} failed!!!", message, topic);
                System.out.println("send fail");
            }
        }
        );
    }

    public void send2group(ChatMsg chatMsg) {
        String group = chatMsg.getGroup();
        List<GroupUserPo> poList = groupUserMapper.findByGroup(group);
        for(GroupUserPo po : poList) {
            chatMsg.setReceiver(po.getUser());
            send2person(chatMsg);
        }
    }

    public List<Msg> getUnsent(String user) {
        return msgDao.findByReceiverAndIsSent(user, false);
    }
}
