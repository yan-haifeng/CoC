package com.coc.auth.service.impl;

import com.coc.auth.service.LoginUserService;
import com.coc.auth.utils.IpUtils;
import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.middleware.pojo.domian.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * LoginUser生成与插入redis
 */
@Service
public class LoginUserServiceImpl implements LoginUserService {

    @Override
    public LoginUser createLoginUser(User user, HttpServletRequest request){
        LoginUser loginUser = new LoginUser().setToken(UUID.randomUUID().toString()).setUser(user);
        loginUser.setLoginIp(IpUtils.getIpAddr(request));
        return loginUser;
    }
}
