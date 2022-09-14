package com.coc.auth.service;

import com.coc.auth.pojo.dto.LoginDto;
import com.coc.remote.dto.UserDto;

public interface PasswordUserService {
    boolean checkUser(LoginDto loginDto, UserDto userDto);
}
