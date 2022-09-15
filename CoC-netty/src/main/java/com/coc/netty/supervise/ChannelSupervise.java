package com.coc.netty.supervise;

import com.coc.netty.service.MsgService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class ChannelSupervise {
    private static final ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap<>();

    @Resource
    private MsgService msgService;

    public  void addChannel(Channel channel, Long userId){
        GlobalGroup.add(channel);
        // 添加用户id
        ChannelMap.put(userId.toString(),channel.id());
    }
    public void removeChannel(Channel channel, Long userId){
        GlobalGroup.remove(channel);
        ChannelMap.remove(userId);
    }
    public Channel findChannel(String id){
        return GlobalGroup.find(ChannelMap.get(id));
    }
    public void sendAll(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
        log.info("群发送一条消息" + tws);
    }
    public Boolean isActive(String receiveUserId){
        return !(ChannelMap.get(receiveUserId) == null);
    }

    /**
     * 发送消息
     * @return 用户在线返回1，否则返回0
     */
    public int send(TextWebSocketFrame tws, String receiveUserId){
        // 用户在线直接发送消息
        if(isActive(receiveUserId)) {
            // 获得接收人的channel
            log.info("用户在线:发送一条消息" + tws);
            Channel channel = this.findChannel(receiveUserId);
            channel.writeAndFlush(tws);
            return 1;
        }
        // 用户不在线持久化消息
        return 0;
    }
}
