package com.example.chat;

import com.example.core.element.Config;
import com.example.core.utils.IpUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                        .connectString(Config.ZOOKEEPER_HOST+":"+Config.ZOOKEEPER_PORT)
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
