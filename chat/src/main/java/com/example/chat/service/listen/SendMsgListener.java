package com.example.chat.service.listen;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;


@Service
public class SendMsgListener {
    private DefaultMQPushConsumer consumer;

    @Autowired
    private Jedis jedis;

    @Value("127.0.0.1:9876")
    private String nameServer;

    private String topic = IpUtils.getPrivateIp() + "netty";

    @PostConstruct
    public void init() throws Exception {
        consumer = new DefaultMQPushConsumer("consumerGroup");
        consumer.setNamesrvAddr(nameServer);
        subscribeToTopic(topic);
        consumer.start();
    }

    public void subscribeToTopic(String topic) throws Exception {
        consumer.unsubscribe(consumer.getSubscription().keySet().iterator().next());
        consumer.subscribe(topic, "*");

        MessageListenerOrderly messageListenerOrderly = new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                // 处理逻辑
                for (MessageExt messageExt : list) {
                    ChatMsg chatMsg = JsonUtils.json2Obj(new String(messageExt.getBody()), ChatMsg.class);
                    Channel channel = UserChannel.get(chatMsg.getReceiver());
                    if (channel != null) {
                        if (ChatHandler.users.contains(channel)) {
                            channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.obj2Json(chatMsg)));
                        }
                    }
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        };

        consumer.setMessageListener(messageListenerOrderly);
    }

    @PreDestroy
    public void destroy() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
