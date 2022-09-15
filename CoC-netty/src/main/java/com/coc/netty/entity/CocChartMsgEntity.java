package com.coc.netty.entity;

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
 * CocChartMsgEntity: 数据映射实体定义
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
    table = "coc_chart_msg",
    schema = "CoC",
    desc = "聊天消息表"
)
public class CocChartMsgEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId("id")
  private Integer id;

  @TableField(
      value = "create_time",
      desc = "创建时间"
  )
  private Date createTime;

  @TableField(
      value = "is_delete",
      desc = "是否删除 0：正常 1：删除"
  )
  private Integer isDelete;

  @TableField(
      value = "msg",
      desc = "消息"
  )
  private String msg;

  @TableField(
      value = "receive_user_id",
      desc = "接收者id"
  )
  private Long receiveUserId;

  @TableField(
      value = "send_time",
      desc = "发送时间"
  )
  private Date sendTime;

  @TableField(
      value = "send_user_id",
      desc = "发送者id"
  )
  private Long sendUserId;

  @TableField(
      value = "status",
      desc = "状态 0：未接收 1：已接收"
  )
  private Integer status;

  @TableField(
      value = "update_time",
      desc = "更新时间"
  )
  private Date updateTime;

  @Override
  public final Class entityClass() {
    return CocChartMsgEntity.class;
  }
}
