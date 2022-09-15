package com.coc.user.service;

import com.coc.user.pojo.dto.UserDto;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     */
    UserDto findUserByUserName(String userName);

    /**
     * 根据用户id查询用户信息
     */
    UserDto findUserByUserId(Long userId);
}
