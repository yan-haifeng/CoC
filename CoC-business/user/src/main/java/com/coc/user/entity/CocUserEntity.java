package com.coc.user.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * CocUserEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
    table = "coc_user",
    schema = "CoC",
    desc = "用户表第一版"
)
public class CocUserEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键"
  )
  private Integer id;

  @TableField(
      value = "create_time",
      insert = "now()",
      desc = "创建时间"
  )
  private Date createTime;

  @TableField(
      value = "is_delete",
      insert = "0",
      desc = "是否删除 0：正常 1：删除"
  )
  private Integer isDelete;

  @TableField(
      value = "nick_name",
      desc = "用户昵称"
  )
  private String nickName;

  @TableField(
      value = "password",
      desc = "用户密码"
  )
  private String password;

  @TableField(
      value = "update_time",
      insert = "now()",
      update = "now()",
      desc = "更新时间"
  )
  private Date updateTime;

  @TableField(
      value = "user_id",
      desc = "用户id"
  )
  private Long userId;

  @TableField(
      value = "user_name",
      desc = "用户名"
  )
  private String userName;

  @TableField(
          value = "status",
          insert = "0",
          desc = "账号状态 0：正常 1：停用"
  )
  private Integer status;

  @Override
  public final Class entityClass() {
    return CocUserEntity.class;
  }
}
