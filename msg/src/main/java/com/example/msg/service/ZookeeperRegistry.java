package com.example.msg.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperRegistry {
    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    @Value("${server.port}")
    private int port;

    private static CuratorFramework client;

    @PostConstruct
    public void register() throws Exception {
        System.out.println("zookeeperAddress: " + zookeeperAddress);
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .sessionTimeoutMs(60*1000)
                .connectionTimeoutMs(15*1000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("msg")
                .build();
        client.start();
        String path = "/msgService-" + "60.204.144.112" + "-" + port;
        client.create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path);
        System.out.println("zookeeper -> create a node : " + path);
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }
}
