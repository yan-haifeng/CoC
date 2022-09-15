package com.coc.middleware.utils;

import com.coc.middleware.pojo.domian.LoginUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuthUtils {
    @Resource
    private RedisTemplate<String,LoginUser> redisTemplate;

    public LoginUser getLoginUser(String token) throws RuntimeException {
        if(token != null) {
            LoginUser loginUser = redisTemplate.opsForValue().get(token);
            if (loginUser != null) {
                return loginUser;
            }
        }
        throw new RuntimeException("不存在该token");
    }
}
