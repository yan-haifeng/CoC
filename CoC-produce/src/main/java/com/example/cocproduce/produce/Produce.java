package com.example.cocproduce.produce;

import com.example.cocproduce.entity.User;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Produce {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic}")
    private String topic;
    // HAIFENG-TOPICAAAA

    public void sendMessage(User user){
        SendResult sendResult = rocketMQTemplate.syncSend(topic, user);
    }
}
