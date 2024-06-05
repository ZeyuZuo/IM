package com.example.msg.service.listen;

import com.alibaba.fastjson.JSONObject;
import com.example.msg.dao.MsgDao;
import com.example.msg.dao.bo.Msg;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageListener {
    private DefaultMQPushConsumer consumer;

    @Autowired
    private MsgDao msgDao;

    @Value("${rocketmq.server}")
    private String nameServer;

    private final String topic = "storageHH";

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("testZZY");
        consumer.setNamesrvAddr(nameServer);
        consumer.subscribe(topic, "*");
        MessageListenerOrderly listener = new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt msgExt : list) {
                    try {
                        System.out.println(new String(msgExt.getBody()));
                        Msg msg = JSONObject.parseObject(new String(msgExt.getBody()), Msg.class);
                        msgDao.save(msg);
                    } catch (Exception e) {
                        e.printStackTrace(); // 打印错误堆栈信息
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT; // 暂停消费当前队列一段时间
                    }
                }
                System.out.println("out of for");
                return ConsumeOrderlyStatus.SUCCESS;
            }
        };
        consumer.registerMessageListener(listener);
        consumer.start();
        System.out.println("connect to rocketMQ, nameSrv is " + nameServer);
        System.out.println("consumer started, topic is " + topic);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("shutdown rocketMQ");
        if (consumer != null) {
            consumer.shutdown();
            // log.debug("rocketMQ consumer shutdown");
        }
    }
}
