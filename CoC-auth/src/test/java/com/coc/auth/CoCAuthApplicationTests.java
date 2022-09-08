package com.coc.auth;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import com.coc.auth.entity.CocUserEntity;
import com.coc.auth.mapper.CocUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CoCAuthApplicationTests {

    // 数据源 url
    static final String url = "jdbc:mysql://110.40.214.91:3306/CoC?useUnicode=true&characterEncoding=utf8";
    // 数据库用户名
    static final String username = "root";
    // 数据库密码
    static final String password = "haifeng328";

    @Resource
    private CocUserMapper cocUserMapper;

    @Test
    public void test1(){
        CocUserEntity cocUser = new CocUserEntity().setUserId(1265463241143242L).setUserName("admin").setNickName("admin").setPassword("123123123");
        cocUserMapper.insert(cocUser);
    }

    @Test
    public void generate() {
        // 引用配置类，build方法允许有多个配置类
        FileGenerator.build(Empty.class);
    }

    @Tables(
            // 设置数据库连接信息
            url = url, username = username, password = password,
            // 设置entity类生成src目录, 相对于 user.dir
            srcDir = "src/main/java",
            // 设置entity类的package值
            basePack = "com.coc.auth",
            // 设置dao接口和实现的src目录, 相对于 user.dir
            daoDir = "src/main/java",
            // 设置哪些表要生成Entity文件
            tables = {@Table(value = {"coc_user"})}
    )
    static class Empty { //类名随便取, 只是配置定义的一个载体
    }

}
