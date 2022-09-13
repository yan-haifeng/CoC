package com.coc.auth.controller;

import com.coc.auth.pojo.dto.LoginDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class TokenController {

    /**
     * 登录
     */
    @GetMapping("/login")
    public String login(@Valid LoginDto dto){
        return "login";
    }
}
