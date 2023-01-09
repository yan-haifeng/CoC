package com.coc.user.service;

import com.coc.user.pay.core.exception.PayException;
import com.coc.user.pojo.dto.UserDto;

import java.util.List;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     */
    UserDto findUserByUserName(String userName);

    /**
     * 根据用户id查询用户信息
     */
    UserDto findUserByUserId(Long userId);

    /**
     * 查询所有用户信息
     */
    List<UserDto> findUser();

    /**
     * 支付Test1
     */
    String payTest() throws PayException;
}
