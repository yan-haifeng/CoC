package com.coc.remote.client;

import com.coc.remote.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "coc-business-user", path = "/user")
public interface UserClient {
    @GetMapping("/findUserByUserName/{userName}")
    UserDto getUserByUserName(@PathVariable String userName);

    @GetMapping("/findUserByUserId/{userId}")
    UserDto getUserByUserId(@PathVariable Long userId);
}
