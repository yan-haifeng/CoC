package com.coc.auth.service.impl;

import com.coc.auth.constant.LoginConstant;
import com.coc.auth.pojo.dto.LoginDto;
import com.coc.auth.service.LoginUserService;
import com.coc.auth.service.PasswordUserService;
import com.coc.auth.service.TokenService;
import com.coc.auth.utils.JwtUtil;
import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.middleware.pojo.domian.User;
import com.coc.remote.client.UserClient;
import com.coc.remote.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Resource
    private UserClient userClient;

    @Resource
    private PasswordUserService passwordUserService;

    @Resource
    private LoginUserService loginUserService;

    @Resource
    private RedisTemplate<String, LoginUser> redisTemplate;

    @Override
    public LoginUser login(LoginDto dto, HttpServletRequest request) throws LoginException {
        // 1. 查用户信息
        UserDto userDto = userClient.getUserByUserName(dto.getUsername());
        // 2. 用户不存在
        if (StringUtils.isEmpty(userDto.getUserName())){
            log.info("登录用户不存在");
            throw new LoginException("用户不存在");
        }
        // 3. 校验是否逻辑删除
        if (LoginConstant.DELETE.equals(userDto.getIsDelete())){
            log.info("用户"+userDto.getUserId()+"已被删除");
            throw new LoginException("用户已被删除");
        }
        // 4. 校验账号状态是否正常
        if(LoginConstant.UN_NORMAL.equals(userDto.getStatus())){
            log.info("用户"+userDto.getUserId()+"已停用");
            throw new LoginException("该账户已停用");
        }
        // 5. 校验账号密码
        if(!passwordUserService.checkUser(dto, userDto)){
            log.info("用户"+userDto.getUserId()+"密码不正确");
            throw new LoginException("密码不正确");
        }
        // 6. 生成LoginUser
        User user = new User();
        try {
            BeanUtils.copyProperties(user, userDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("传递参数不正确");
            throw new LoginException("传递参数不正确");
        }
        // 7. 返回LoginUser
        return loginUserService.createLoginUser(user, request);
    }

    /**
     * 获取token
     */
    @Override
    public String getToken(LoginUser loginUser){
        // 设置token时间
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expTime = nowTime.plus(10, ChronoUnit.MINUTES);
        Date now = Date.from(nowTime.toInstant(ZoneOffset.ofHours(8)));
        Date exp = Date.from(expTime.toInstant(ZoneOffset.ofHours(8)));
        // 设置loginUser过期时间
        loginUser.setExpiration(exp);
        // 获取token
        String token = JwtUtil.createJWT(loginUser.getToken(), loginUser, now, exp);
        // 将token存入redis
        redisTemplate.opsForValue().set(token, loginUser, 10, TimeUnit.MINUTES);
        return token;
    }
}
