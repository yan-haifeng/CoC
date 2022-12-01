package com.coc.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.coc.auth.pojo.dto.LoginDto;
import com.coc.auth.service.TokenService;
import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.middleware.utils.R;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin
    @PostMapping("/login")
    public R login(@RequestBody @Valid LoginDto dto, HttpServletRequest request) throws LoginException {
        LoginUser loginUser = tokenService.login(dto, request);
        //获取token
        String token = tokenService.getToken(loginUser);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("loginUser", loginUser);
        jsonObject.put("access_token", token);
        return R.ok().data(jsonObject);
    }

}
