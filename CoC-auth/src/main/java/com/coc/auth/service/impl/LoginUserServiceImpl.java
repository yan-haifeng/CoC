package com.coc.auth.service.impl;

import com.coc.auth.pojo.domain.LoginUser;
import com.coc.auth.pojo.domain.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * LoginUser生成与插入redis
 */
@Service
public class LoginUserServiceImpl {
    @Resource
    private RedisTemplate redisTemplate;

    public LoginUser createLoginUser(User user){
        LoginUser loginUser = new LoginUser().token(UUID.randomUUID().toString()).user(user);
        return loginUser;
    }

}
