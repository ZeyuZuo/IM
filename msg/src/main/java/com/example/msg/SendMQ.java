//package com.example.msg;
//
//import com.alibaba.fastjson.JSONObject;
//import com.example.core.element.ChatMsg;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.client.producer.SendCallback;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import redis.clients.jedis.Jedis;
//
//public class SendMQ {
//
//
//    public static void main(String[] args) throws Exception {
//        String nameStr = "1.94.30.190:9876";
//        String producerGroup = "msg";
//
//        Jedis jedis = new Jedis("1.94.110.161",1968);
//
//        ChatMsg chatMsg = new ChatMsg("zzy", "xzx", "hello");
//        System.out.println(chatMsg);
//        String ip = jedis.get("xzxNetty");
//        ip = ip.replace(".", "");
//        System.out.println(ip);
//        String topic = ip + "netty";
//        System.out.println(topic);
//
//        String msg = JSONObject.toJSONString(chatMsg);
//        System.out.println(msg);
//
//
//
//
//        DefaultMQProducer producer = new DefaultMQProducer();
//        producer.setProducerGroup(producerGroup);
//        producer.setNamesrvAddr(nameStr);
//
//        RocketMQTemplate template = new RocketMQTemplate();
//        producer.start();
//        template.setProducer(producer);
//
//        template.asyncSendOrderly(topic, msg, "0", new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                System.out.println("send success!");
//                System.out.println(sendResult);
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                System.out.println("send fail");
//                System.out.println(throwable.getStackTrace());
//            }
//        });
//
//        // producer.start();//启动生产者
////        Message message = new Message(topic, msg.getBytes());
////        producer.send(message, new SendCallback() {
////            @Override
////            public void onSuccess(SendResult sendResult) {
////                System.out.println("send success!");
////                System.out.println(sendResult);
////            }
////
////            @Override
////            public void onException(Throwable throwable) {
////                System.out.println("send fail");
////                System.out.println(throwable.getStackTrace());
////            }
////        });
//
//
//
//
//
//    }
//}
