package com.coc.netty.handler;

import com.alibaba.fastjson.JSON;
import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.netty.pojo.dto.MsgDto;
import com.coc.netty.pojo.vo.MsgVo;
import com.coc.netty.service.MsgService;
import com.coc.netty.supervise.ChannelSupervise;
import com.coc.remote.client.UserClient;
import com.coc.remote.dto.UserDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

@Slf4j
@Component
@Scope("prototype")
public class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;
    @Resource
    private ChannelSupervise channelSupervise;
    @Resource
    private MsgService msgService;
    @Resource
    private UserClient userClient;

    private LoginUser loginUser;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("收到消息："+msg);
        if (msg instanceof FullHttpRequest){
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.debug("客户端加入连接："+ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.debug("客户端断开连接："+ctx.channel());
        channelSupervise.removeChannel(ctx.channel(), loginUser.getUser().getUserId());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 处理websocket客户端的消息
     * */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.debug("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 获得客户端发送的消息
        String text = ((TextWebSocketFrame) frame).text();
        MsgDto msgDto = JSON.parseObject(text, MsgDto.class);
        log.debug("服务端收到：" + msgDto);

        // 处理返回的消息
        MsgVo msgVo = msgService.parseMsg(msgDto, loginUser.getUser().getNickName());
        TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(msgVo));
        // 群发
        // channelSupervise.sendAll(tws);
        // 单发【谁发的发给谁】
        if(channelSupervise.send(tws, msgDto.getReceiveUserId()) == 0){
            // 判断用户是否存在
            UserDto userDto = userClient.getUserByUserId(Long.parseLong(msgDto.getReceiveUserId()));
            if (userDto != null){
                log.info("用户不在线：存储一条消息" + tws);
                msgService.saveMsg(msgDto, loginUser);
            }else{
                ctx.channel().writeAndFlush("不存在该用户");
            }
        }
    }

    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8888/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
        // 获取请求头里的token信息
        // 连接用户的token
        String token = req.headers().get("Authorization");
        try{
            loginUser = msgService.getLoginUser(token);
        }catch (RuntimeException e){
            //不存在该token
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
            return;
        }
        // 将用户添加到在线管理容器
        channelSupervise.addChannel(ctx.channel(), loginUser.getUser().getUserId());
        // 查询该用户在下线时的接收消息
        Map<Long, List<MsgVo>> msg = msgService.findUnreadMsg(loginUser);
        if(msg.isEmpty()){
            return;
        }
        TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(msg));
        channelSupervise.send(tws, loginUser.getUser().getUserId().toString());
    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     * */
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
