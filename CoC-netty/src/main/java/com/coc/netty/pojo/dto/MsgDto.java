package com.coc.netty.pojo.dto;

import lombok.Data;

@Data
public class MsgDto {

    /**
     * 接收人的userId
     */
    private String receiveUserId;

    /**
     * 发送的消息
     */
    private String msg;
}
