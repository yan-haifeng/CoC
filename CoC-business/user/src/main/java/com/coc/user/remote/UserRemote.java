package com.coc.user.remote;

import com.coc.user.pojo.dto.UserDto;
import com.coc.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/findUserByUserName/{userName}")
    public UserDto getUserByUserName(@PathVariable String userName){
        return userService.findUserByUserName(userName);
    }

    @GetMapping("/findUserByUserId/{userId}")
    public UserDto getUserByUserId(@PathVariable Long userId){
        return userService.findUserByUserId(userId);
    }
}
