package com.coc.auth.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true,fluent = true)
public class LoginDto {


    /* 用户名 */
    @NotNull(message = "用户名不能为空")
    @Size(min = 4, max = 13, message = "用户名长度在4-13位之间")
    private String username;

    /* 密码 */
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 13, message = "密码长度在6-13位之间")
    @Pattern(regexp = "^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{6,}$", message = "密码应为数字、大小写字母、特殊字符中的至少3种组成")
    private String password;
}
