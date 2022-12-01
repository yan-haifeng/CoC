package com.coc.auth.service.impl;

import com.coc.auth.pojo.dto.LoginDto;
import com.coc.auth.service.PasswordUserService;
import com.coc.remote.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class PasswordUserServiceImpl implements PasswordUserService {

    /**
     * 校验用户信息
     */
    @Override
    public boolean checkUser(LoginDto loginDto, UserDto userDto){
        if (loginDto.getPassword() == null || userDto.getPassword() == null) {
            return false;
        }
        return loginDto.getPassword().equals(userDto.getPassword());
    }
}
