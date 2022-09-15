package com.coc.middleware.pojo.domian;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class LoginUser {

    /**
     * 本次登录的唯一标识
     */
    private String token;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录过期时间
     */
    private Date expiration;
}
