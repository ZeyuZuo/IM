//package com.example.gateway.service;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MsgFind {
//
//    Logger logger = LoggerFactory.getLogger(MsgFind.class);
//
//    private List<String> msgServices = new ArrayList<String>();
//    private int index = 0;
//
//    @Value("${zookeeper.address}")
//    private String zookeeperAddress;
//
//    @PostConstruct
//    public void init() {
//        try{
//            CuratorFramework client = CuratorFrameworkFactory.builder()
//                    .connectString(zookeeperAddress)
//                    .sessionTimeoutMs(60*1000)
//                    .connectionTimeoutMs(15*1000)
//                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
//                    .namespace("msg")
//                    .build();
//            client.start();
//
//            List<String> msgList = client.getChildren().forPath("/");
//
//            if(msgList != null) {
//                for(String msg : msgList) {
//                    msgServices.add(msg.split("-")[1]);
//                }
//            }else{
//                System.out.println("no msg service found");
//            }
//        }catch(Exception e){
//            logger.info("error when register msg service : {}", e.getMessage());
//            System.out.println("error when register msg service");
//        }
//
//        outputMsgServices();
//    }
//
//    public String getMsgService() {
//
//        outputMsgServices();
//        System.out.println(index);
//
//        String ip = msgServices.get(index++);
//        if(index == msgServices.size()) {
//            index = 0;
//        }
//
//        System.out.println(ip);
//        return ip;
//    }
//
//    public void register(String ip){
//        msgServices.add(ip);
//    }
//
//    public void unregister(String ip){
//        msgServices.remove(ip);
//    }
//
//    public void outputMsgServices() {
//        for(String msgService : msgServices) {
//            System.out.println(msgService);
//        }
//    }
//}
