package com.coc.user.remote;

import com.coc.user.pojo.dto.UserDto;
import com.coc.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户服务对其他服务暴露的接口
 */
@RestController
@RequestMapping("/user")
public class UserRemote {

    @Resource
    private UserService userService;

    @PostMapping("/findUserByUserName")
    public UserDto getUserByUserName(String userName){
        return userService.findUserByUserName(userName);
    }
}
