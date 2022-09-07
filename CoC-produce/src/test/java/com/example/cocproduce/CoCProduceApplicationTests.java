package com.example.cocproduce;

import com.example.cocproduce.entity.User;
import com.example.cocproduce.produce.Produce;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CoCProduceApplicationTests {

    @Resource
    private Produce produce;

    @Test
    void contextLoads() {
        produce.sendMessage(new User("123","李明"));
    }

}
