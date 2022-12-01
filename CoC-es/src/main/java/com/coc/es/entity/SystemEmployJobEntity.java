package com.coc.es.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * SystemEmployJobEntity: 数据映射实体定义
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
    table = "system_employ_job",
    schema = "CoC",
    desc = "招聘功能-岗位表"
)
public class SystemEmployJobEntity extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "job_id",
      desc = "岗位id"
  )
  private Long jobId;

  @TableField(
      value = "company_user_id",
      desc = "企业用户关联id"
  )
  private Long companyUserId;

  @TableField(
      value = "create_by",
      desc = "创建人"
  )
  private String createBy;

  @TableField(
      value = "create_time",
      desc = "创建时间"
  )
  private Date createTime;

  @TableField(
      value = "deleted",
      desc = "有效标记; 0-有效 1-无效"
  )
  private Integer deleted;

  @TableField(
      value = "high_salary",
      desc = "薪资范围；高"
  )
  private Long highSalary;

  @TableField(
      value = "job_desc",
      desc = "职位描述"
  )
  private String jobDesc;

  @TableField(
      value = "job_keyword",
      desc = "职位关键词"
  )
  private String jobKeyword;

  @TableField(
      value = "job_name",
      desc = "职位名称"
  )
  private String jobName;

  @TableField(
      value = "job_state",
      desc = "职位状态；0-招聘中 1-已下架"
  )
  private Integer jobState;

  @TableField(
      value = "job_type",
      desc = "工期类型；0-全职 1-兼职"
  )
  private Integer jobType;

  @TableField(
      value = "lat",
      desc = "纬度"
  )
  private String lat;

  @TableField(
      value = "lng",
      desc = "经度"
  )
  private String lng;

  @TableField(
      value = "low_edu",
      desc = "最低学历；0-不限 1-初中 2-中专 3-高中 4-大专 5-本科 6-硕士及以上"
  )
  private Integer lowEdu;

  @TableField(
      value = "low_salary",
      desc = "薪资范围；低"
  )
  private Long lowSalary;

  @TableField(
      value = "req_exper",
      desc = "经验要求；0-经验不限 1：1年以内 2：1-3年 3：3-5年 4：5年以上"
  )
  private Integer reqExper;

  @TableField(
      value = "salary_type",
      desc = "薪资类型；0-月薪 1-日薪 2-时薪 3-次薪"
  )
  private Integer salaryType;

  @TableField(
      value = "select_industry",
      desc = "选择行业"
  )
  private String selectIndustry;

  @TableField(
      value = "update_by",
      desc = "修改人"
  )
  private String updateBy;

  @TableField(
      value = "update_time",
      desc = "修改时间"
  )
  private Date updateTime;

  @TableField(
      value = "work_address",
      desc = "工作地址"
  )
  private String workAddress;

  @Override
  public final Class entityClass() {
    return SystemEmployJobEntity.class;
  }
}
