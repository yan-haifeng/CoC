package com.coc.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @ClassName: Job
 * @Describe:
 * @Author: fanchenyang
 * @Date: 2022/10/11 9:50
 * @Version 1.0
 **/

@Data
@Document(indexName = "#{@indexJobName}")
public class Job {
    /**
     * 岗位id
     */
    @Id
    @Field(name = "job_id", type = FieldType.Long)
    private Long jobId;

    /**
     * 企业用户关联id
     */
    @Field(name = "company_user_id", type = FieldType.Long)
    private Long companyUserId;

    /**
     * 职位名称
     */
    @Field(name = "job_name", type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String jobName;

    /**
     * 工期类型；0-全职 1-兼职
     */
    @Field(name = "job_type", type = FieldType.Integer)
    private Integer jobType;

    /**
     * 选择行业
     */
    @Field(name = "select_industry", type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String selectIndustry;

    /**
     * 职位描述
     */
    @Field(name = "job_desc", type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String jobDesc;

    /**
     * 经验要求；0-不限 1：1年以内 2：1-3年 3：3-5年 4：5年以上
     */
    @Field(name = "req_exper", type = FieldType.Integer)
    private Integer reqExper;

    /**
     * 最低学历；0-不限 1-初中 2-中专 3-高中 4-大专 5-本科 6-硕士及以上
     */
    @Field(name = "low_edu", type = FieldType.Integer)
    private Integer lowEdu;

    /**
     * 薪资范围；低
     */
    @Field(name = "low_salary", type = FieldType.Long)
    private Long lowSalary;

    /**
     * 薪资范围；高
     */
    @Field(name = "high_salary", type = FieldType.Long)
    private Long highSalary;

    /**
     * 薪资类型；0-月薪 1-日薪 2-时薪
     */
    @Field(name = "salary_type", type = FieldType.Integer)
    private Integer salaryType;

    /**
     * 职位关键词
     */
    @Field(name = "job_keyword", type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String jobKeyword;

    /**
     * 工作地址
     */
    @Field(name = "work_address", type = FieldType.Text)
    private String workAddress;

    /**
     * 工作地址经度
     */
    @Field(name = "lng", type = FieldType.Text)
    private String lng;

    /**
     * 工作地址纬度
     */
    @Field(name = "lat", type = FieldType.Text)
    private String lat;

    /**
     * 职位状态；0-招聘中 1-已下架
     */
    @Field(name = "job_state", type = FieldType.Integer)
    private Integer jobState;

    /**
     * 创建人
     */
    @Field(name = "create_by", type = FieldType.Text)
    private String createBy;

    /**
     * 创建时间
     */
//    @Field(type = FieldType.Date, name = "create_time",format = DateFormat.custom,
//            pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd'T'HH:mm:ss'+08:00' || strict_date_optional_time || epoch_millis")
//    private Date createTime;
    @Field(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Field(name = "update_by", type = FieldType.Text)
    private String updateBy;

    /**
     * 修改时间
     */
//    @Field(type = FieldType.Date, name = "update_time",format = DateFormat.custom,
//            pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd'T'HH:mm:ss'+08:00' || strict_date_optional_time || epoch_millis")
//    private Date updateTime;
    @Field(name = "update_time")
    private Date updateTime;

    /**
     * 有效标记; 0-有效 1-无效
     */
    @Field(name = "deleted", type = FieldType.Integer)
    private Integer deleted;
}
