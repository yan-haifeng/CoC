package com.coc.user.controller;

import com.coc.middleware.utils.R;
import com.coc.user.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController()
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @CrossOrigin
    @GetMapping("/list")
    public R getUserList(){
        return R.ok().data(userService.findUser());
    }
}
