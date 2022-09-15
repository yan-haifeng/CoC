package com.coc.netty.task;

import com.coc.netty.server.WebSocketServer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class WebSocketTask{
    @Resource
    private WebSocketServer webSocketServer;

    @PostConstruct
    public void init(){
        Thread t = new Thread(webSocketServer);
        t.start();
    }
}
