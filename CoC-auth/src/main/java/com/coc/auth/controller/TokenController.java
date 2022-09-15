package com.coc.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.coc.auth.pojo.dto.LoginDto;
import com.coc.auth.service.TokenService;
import com.coc.middleware.pojo.domian.LoginUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class TokenController {

    @Resource
    private TokenService tokenService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public JSONObject login(@RequestBody @Valid LoginDto dto, HttpServletRequest request) throws LoginException {
        JSONObject json = new JSONObject();
        LoginUser loginUser = tokenService.login(dto, request);

        //获取token
        String token = tokenService.getToken(loginUser);
        json.put("loginUser", loginUser);
        json.put("access_token", token);
        return json;
    }
}
