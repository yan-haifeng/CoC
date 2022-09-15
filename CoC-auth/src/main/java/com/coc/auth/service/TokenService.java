package com.coc.auth.service;

import com.coc.auth.pojo.dto.LoginDto;
import com.coc.middleware.pojo.domian.LoginUser;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    LoginUser login(LoginDto dto, HttpServletRequest request) throws LoginException;

    String getToken(LoginUser loginUser);
}
