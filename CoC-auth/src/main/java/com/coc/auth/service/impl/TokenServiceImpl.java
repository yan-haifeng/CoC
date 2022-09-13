package com.coc.auth.service.impl;

import com.coc.auth.constant.LoginConstant;
import com.coc.auth.pojo.dto.LoginDto;
import com.coc.auth.service.TokenService;
import com.coc.remote.client.UserClient;
import com.coc.remote.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Resource
    private UserClient userClient;

    public String login(LoginDto dto) throws LoginException {
        // 1. 查用户信息
        UserDto userDto = userClient.getUserByUserName(dto.username());
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

        // 6. 生成LoginUser

        // 7. 返回LoginUser

        return "亏贼";
    }
}
