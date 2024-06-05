package com.example.gateway.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MsgFind {

    Logger logger = LoggerFactory.getLogger(MsgFind.class);

    private static List<String> msgServices = new ArrayList<String>();
    private static int index = 0;

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    @PostConstruct
    public void init() {
        try{
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperAddress)
                    .sessionTimeoutMs(60*1000)
                    .connectionTimeoutMs(15*1000)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .namespace("msg")
                    .build();
            client.start();

            List<String> msgList = client.getChildren().forPath("/");

            if(msgList != null) {
                for(String msg : msgList) {
                    msgServices.add(msg.split("-")[1]);
                }
            }else{
                System.out.println("no msg service found");
            }
        }catch(Exception e){
            logger.info("error when register msg service : {}", e.getMessage());
            System.out.println("error when register msg service");
        }
    }

    public static String getMsgService() {
        return msgServices.get(index++);
    }

    public static void register(String ip){
        msgServices.add(ip);
    }

    public static void unregister(String ip){
        msgServices.remove(ip);
    }

    public static void outputMsgServices() {
        for(String msgService : msgServices) {
            System.out.println(msgService);
        }
    }
}
