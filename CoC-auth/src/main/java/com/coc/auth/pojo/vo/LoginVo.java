package com.coc.auth.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true,fluent = true)
public class LoginVo {
    private String token;
}
