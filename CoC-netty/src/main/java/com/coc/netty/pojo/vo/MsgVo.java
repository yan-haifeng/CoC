package com.coc.netty.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MsgVo {
    /**
     * 发送人名字
     */
    private String sendUserTaskName;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送消息
     */
    private String msg;
}
