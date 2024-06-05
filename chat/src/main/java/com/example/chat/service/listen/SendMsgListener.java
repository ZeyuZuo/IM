package com.example.chat.service.listen;

import com.alibaba.fastjson.JSONObject;
import com.example.chat.netty.UserChannel;
import com.example.chat.netty.handler.ChatHandler;
import com.example.core.element.ChatMsg;
import com.example.core.utils.IpUtils;
import com.example.core.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;


@Service
public class SendMsgListener {
    // private static final Logger log = LoggerFactory.getLogger(SendMsgListener.class);
    private DefaultMQPushConsumer consumer;

    @Value("${rocketmq.server}")
    private String nameServer;

    private String topic = IpUtils.getPrivateIp().replace(".","") + "netty";

    @PostConstruct
    public void init() throws Exception {
        consumer = new DefaultMQPushConsumer(topic);
        consumer.setNamesrvAddr(nameServer);
        subscribeToTopic(topic);
        consumer.start();
        System.out.println("connect to rocketMQ, nameSrv is " + nameServer);
        System.out.println("consumer started, topic is " + topic);
    }

    private void subscribeToTopic(String topic) throws Exception {
        consumer.subscribe(topic, "*");

        MessageListenerOrderly messageListenerOrderly = new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                // 处理逻辑
                for (MessageExt messageExt : list) {
                    System.out.println("------------------------------------------------");
                    System.out.println("receive message: " + new String(messageExt.getBody()));
                    ChatMsg chatMsg = JSONObject.parseObject(new String(messageExt.getBody()), ChatMsg.class);
                    System.out.println("chatMsg : " + chatMsg);
                    Channel channel = UserChannel.get(chatMsg.getReceiver());
                    System.out.println("channel : " + channel);
                    if (channel != null) {
                        if (ChatHandler.users.contains(channel)) {
                            String msg = JSONObject.toJSONString(chatMsg);
                            System.out.println("send message : " + msg);
                            channel.writeAndFlush(new TextWebSocketFrame(msg));
                        }
                    }
                    System.out.println("------------------------------------------------");
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        };

        consumer.setMessageListener(messageListenerOrderly);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("hello");
        if (consumer != null) {
            consumer.shutdown();
            System.out.println("consumer shutdown");
            // log.debug("rocketMQ consumer shutdown");
        }
    }
}
