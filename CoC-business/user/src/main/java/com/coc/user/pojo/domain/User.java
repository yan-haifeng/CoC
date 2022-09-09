package com.coc.user.pojo.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    /* id */
    private Integer id;

    /* 用户id */
    private Long userId;

    /* 用户名 */
    private String userName;

    /* 密码 */
    private String password;

    /* 用户昵称 */
    private String nickName;

    /* 账号状态 */
    private Integer status;

    /* 创建时间 */
    private Date createTime;

    /* 更新时间 */
    private Date updateTime;

    /* 逻辑删除标志 */
    private Integer isDelete;
}
