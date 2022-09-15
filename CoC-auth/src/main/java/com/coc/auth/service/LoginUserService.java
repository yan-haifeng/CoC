package com.coc.auth.service;

import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.middleware.pojo.domian.User;

import javax.servlet.http.HttpServletRequest;

public interface LoginUserService {
    LoginUser createLoginUser(User user, HttpServletRequest request);
}
