package com.example.msg.config;

import ch.qos.logback.core.model.processor.DefaultProcessor;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RocketMQConfig {
    private Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    @Value("127.0.0.1:12347")
    private String nameStr;

    @Value("msg")
    private String producerGroup;

    @Bean
    public RocketMQTemplate rocketMQTemplate(){
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(this.producerGroup);
        producer.setNamesrvAddr(this.nameStr);
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(producer);
        return template;
    }
}
