package com.example.cocrocketmq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.topic}",consumerGroup = "HF_CONSUMER_1")
public class ConsumerOne implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("Consumer1:" + s);
    }
}
