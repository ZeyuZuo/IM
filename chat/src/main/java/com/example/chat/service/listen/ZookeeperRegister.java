package com.example.chat.service.listen;

import com.example.core.utils.IpUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperRegister {
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
                .namespace("netty")
                .build();
        client.start();
        /**
         * nettyService-1.94.26.215-8082
         */
        String path = "/nettyService-" + IpUtils.getPublicIP() + "-" + port;
        client.create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path);
        System.out.println("zookeeper -> create a node : " + path);
    }

    @PreDestroy
    public void destroy() {
        if(null != client) {
            client.close();
        }
    }

}
