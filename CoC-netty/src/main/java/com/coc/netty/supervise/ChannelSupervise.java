package com.coc.netty.supervise;

import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.middleware.utils.AuthUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChannelSupervise {
    private static ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap();

    @Resource
    private AuthUtils authUtils;
    public  void addChannel(Channel channel, String token){
        GlobalGroup.add(channel);
        //获取用户id
        LoginUser loginUser = authUtils.getLoginUser(token);
        // 添加用户id
        ChannelMap.put(loginUser.getUser().getUserId().toString(),channel.id());
    }
    public void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id().asShortText());
    }
    public Channel findChannel(String id){
        return GlobalGroup.find(ChannelMap.get(id));
    }
    public void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
}
