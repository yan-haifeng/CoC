package com.coc.remote.client;

import com.coc.remote.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "coc-business-user")
public interface UserClient {
    @PostMapping ("/user/findUserByUserName")
    UserDto getUserByUserName(String userName);
}
