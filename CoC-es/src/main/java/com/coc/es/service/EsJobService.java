package com.coc.es.service;

import com.coc.es.entity.Job;
import com.coc.es.entity.SystemEmployJobEntity;
import com.coc.es.mapper.SystemEmployJobMapper;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EsJobService {
    @Resource
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private SystemEmployJobMapper systemEmployJobMapper;

    @Resource
    private RedisTemplate<String, Date> redisTemplate;

    @Value("${spring.elasticsearch.synchroniz_form.system_employ_job}")
    private String indexJobName;

    final public String SYNCHRONIZED_SQL_ES_KEY = "synchronized_sql_es";
    final public String LAST_TIME_EMPLOY_JOB_KEY = "system_employ_job_last_time";

    /**
     * 同步job表到es
     */
    public void SynchronizationJobToEs() {
        // 判断是不是第一次启动该程序，如果是，要初始化时间参数且把数据库数据全部同步到es
        // 判断redis里有没有该键
        Boolean isFirstRun = redisTemplate.opsForHash().hasKey(SYNCHRONIZED_SQL_ES_KEY, LAST_TIME_EMPLOY_JOB_KEY);
        if (!isFirstRun) {
            // 第一次启动该程序
            initEs();
        }
        // 向redis获取上次更新的时间
        Date updateDate = (Date) redisTemplate.opsForHash().get(SYNCHRONIZED_SQL_ES_KEY, LAST_TIME_EMPLOY_JOB_KEY);
        // 向数据库查询更新时间大于等于initDate的数据
        com.coc.es.wrapper.SystemEmployJobQuery systemEmployJobQuery = new com.coc.es.wrapper.SystemEmployJobQuery();
        com.coc.es.wrapper.SystemEmployJobQuery sqlQuery = systemEmployJobQuery.where.updateTime().ge(updateDate).end();
        // 要添加或更新到es的数据
        List<SystemEmployJobEntity> jobEntities = systemEmployJobMapper.listEntity(sqlQuery);

        if (jobEntities != null && !jobEntities.isEmpty()){
            // 有要添加到es的数据
            // 将要添加到es的字段转为es的job实体
            List<IndexQuery> esJobQueryList = new ArrayList<>();
            jobEntities.forEach(s -> {
                Job job = new Job();
                BeanUtils.copyProperties(s, job);
                IndexQuery esQuery = new IndexQueryBuilder().withId(job.getJobId().toString()).withObject(job).build();
                esJobQueryList.add(esQuery);
            });
            // 添加到es
            elasticsearchRestTemplate.bulkIndex(esJobQueryList, Job.class);
        }

        // 对比es与mysql表，检测mysql是否有删除数据
        List<Long> idsDifferenceSet = getJobDifferenceSet();
        if (!idsDifferenceSet.isEmpty()) {
            // 进这个if说明数据库有物理删除数据，es也要对应删除
            IdsQueryBuilder idsQuery = QueryBuilders.idsQuery();
            idsDifferenceSet.forEach(id -> {
                idsQuery.addIds(id.toString());
            });
            elasticsearchRestTemplate.delete(new NativeSearchQuery(idsQuery), Job.class);
        }

        LocalDateTime dateTime = LocalDateTime.now();
        updateDate = getDate(dateTime);;
        // 更新时间传到redis里
        redisTemplate.opsForHash().put(SYNCHRONIZED_SQL_ES_KEY, LAST_TIME_EMPLOY_JOB_KEY, updateDate);
    }

    /**
     * 初始化es
     */
    private <T> void initEs() {
        // 1. 初始化时间
        // 创建 初始时间 1970-01-01 00:00:00
        LocalDateTime dateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        Date initDate = getDate(dateTime);
        // 2.初始化redis键，并将时间传到redis里
        redisTemplate.opsForHash().put(SYNCHRONIZED_SQL_ES_KEY, LAST_TIME_EMPLOY_JOB_KEY, initDate);
        // 3. 初始化es索引
        // 指定文档的数据实体类
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Job.class);
        // 创建索引
        indexOperations.create();
        // 创建字段映射
        Document mapping = indexOperations.createMapping();
        // 给索引设置字段映射
        indexOperations.putMapping(mapping);
    }

    /**
     * LocalDateTime 转 Date
     */
    private Date getDate(LocalDateTime dateTime) {
        // 将此日期时间与时区相结合以创建 ZonedDateTime
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        // 本地时间线LocalDateTime到即时时间线Instant时间戳
        Instant instant = zonedDateTime.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return Date.from(instant);
    }

    /**
     * 获取mysql中job表与es中的id差异（job表是es表的子集）
     */
    private List<Long> getJobDifferenceSet() {
        // 获取job表的所有id
        com.coc.es.wrapper.SystemEmployJobQuery query = new com.coc.es.wrapper.SystemEmployJobQuery();
        Set<Long> set = systemEmployJobMapper.listEntity(query).stream().map(SystemEmployJobEntity::getJobId).collect(Collectors.toSet());
        // 获取es中job索引的所有id
        List<Long> esIdsList = this.getAllDataForEs(100, Job.class, IndexCoordinates.of(indexJobName)).stream().map(Job::getJobId).collect(Collectors.toList());
        // 取它们差集
        esIdsList.removeAll(set);
        return esIdsList;
    }

    /**
     * 获得对应索引下的所有文档
     * @param pageSize 滑动时一次滑动的数据量
     * @param clazz 索引的实体类
     * @param indexName 索引名
     * @return 对应索引的文档列表
     * @param <T> 索引的实体类
     */
    public <T> List<T> getAllDataForEs(Integer pageSize, Class<T> clazz, IndexCoordinates indexName){
        NativeSearchQuery query = new NativeSearchQuery(QueryBuilders.matchAllQuery());
        query.setMaxResults(pageSize);//设置每页数据量

        long scrollTimeInMillis=5_000;
        long currentTotal=0;
        int pageNo=1;
        List<String> scrollIdList = new ArrayList<>();
        List<T> list = new ArrayList<>();

        //第一次查询使用：searchScrollStart
        SearchScrollHits<T> searchScrollHits = this.elasticsearchRestTemplate.searchScrollStart(scrollTimeInMillis, query, clazz, indexName);
        String scrollId = searchScrollHits.getScrollId();
        scrollIdList.add(scrollId);

        long totalHits = searchScrollHits.getTotalHits();
        currentTotal=searchScrollHits.getSearchHits().size();
        list.addAll(searchScrollHits.get().map(SearchHit::getContent).collect(Collectors.toList()));

        while (currentTotal<totalHits){
            SearchScrollHits<T> searchScrollHitsContinue = elasticsearchRestTemplate.searchScrollContinue(scrollId, scrollTimeInMillis, clazz, indexName);
            scrollId=searchScrollHitsContinue.getScrollId();
            scrollIdList.add(scrollId);
            pageNo++;
            if(searchScrollHitsContinue.hasSearchHits()){
                currentTotal+=searchScrollHitsContinue.getSearchHits().size();
                list.addAll(searchScrollHits.get().map(SearchHit::getContent).collect(Collectors.toList()));
            }else{
                break;
            }
        }
        elasticsearchRestTemplate.searchScrollClear(scrollIdList);
        return list;
    }

//    /**
//     * 动态设置es索引实体类上的@Document中indexName的值
//     * @param clazz 索引实体类
//     * @return 索引实体类
//     */
//    public <T> Class<T> getEntityClass(Class<T> clazz) {
//        org.springframework.data.elasticsearch.annotations.Document document = clazz.getAnnotation(org.springframework.data.elasticsearch.annotations.Document.class);
//        InvocationHandler handler = Proxy.getInvocationHandler(document);
//        Field field = null;
//        Map<String, String> map = null;
//        try {
//            field = handler.getClass().getDeclaredField("memberValues");
//            field.setAccessible(true);
//            map = (Map<String, String>) field.get(handler);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//        map.put("indexName", indexJobName);
//        return clazz;
//    }
//
//    // 获得job对象
//    public Job getJobObj(Class<Job> clazz) throws InstantiationException, IllegalAccessException {
//        return clazz.newInstance();
//    }
}
