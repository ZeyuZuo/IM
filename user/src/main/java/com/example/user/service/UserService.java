package com.example.user.service;

import com.example.core.element.Config;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static int selectNettyIndex = 0;

    public String selectNetty() {
        String publicIP = null;

        // 轮询选择netty服务
        try {
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(Config.ZOOKEEPER_HOST+":"+Config.ZOOKEEPER_PORT)
                    .sessionTimeoutMs(60*1000)
                    .connectionTimeoutMs(15*1000)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .namespace("netty")
                    .build();
            client.start();

            List<String> nettyServices = client.getChildren().forPath("/");
            if (selectNettyIndex >= nettyServices.size()) {
                selectNettyIndex = 0;
            }
            publicIP = new String(client.getData().forPath(nettyServices.get(selectNettyIndex++)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return publicIP;
    }
}
