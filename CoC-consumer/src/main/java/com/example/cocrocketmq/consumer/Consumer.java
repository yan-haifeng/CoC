package com.example.cocrocketmq.consumer;

import com.example.cocrocketmq.entity.User;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RocketMQMessageListener(topic = "${rocketmq.topic}",consumerGroup = "${rocketmq.producer.group}")
public class Consumer implements RocketMQListener<User> {
    @Override
    public void onMessage(User u) {
        System.err.println("Consumer:" + u.toString());
    }
}
