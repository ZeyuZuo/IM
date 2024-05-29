package com.example.chat;

import com.example.core.utils.IpUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {

    @Value("${zookeeper.address}")
    private static String zookeeperAddress;

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                        .connectString(zookeeperAddress)
                        .sessionTimeoutMs(60*1000)
                        .connectionTimeoutMs(15*1000)
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .namespace("netty")
                        .build();
        client.start();
        client.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/nettyService-", IpUtils.getPublicIP().getBytes());
        SpringApplication.run(ChatApplication.class, args);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println("Failed to close ZooKeeper client: " + e.getMessage());
            }
        }));
    }
}
