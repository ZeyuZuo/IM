package com.example.msg.config;

import ch.qos.logback.core.model.processor.DefaultProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RocketMQConfig {
    // private Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    @Value("${rocketmq.server}")
    private String nameStr;

    @Value("msg")
    private String producerGroup;

    private DefaultMQProducer producer = null;
    private RocketMQTemplate template = null;

    @Bean
    public RocketMQTemplate rocketMQTemplate() throws MQClientException {
        producer = new DefaultMQProducer();
        producer.setProducerGroup(this.producerGroup);
        producer.setNamesrvAddr(this.nameStr);
        // producer.start();
        template = new RocketMQTemplate();
        template.setProducer(producer);
        return template;
    }

    @PreDestroy
    public void destroy() {
        // logger.info("destroy rocketMQ producer");
        // producer.shutdown();
        if(producer != null) {
            System.out.println("destroy rocketMQ producer");
            producer.shutdown();
        }
    }
}
