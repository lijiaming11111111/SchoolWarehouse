package com.school.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLoginDTO {
    @Schema(description = "账号（手机号或邮箱）", required = true)
    @NotBlank(message = "账号为空")
    private String account;

    @Schema(description = "密码",required = true)
    @NotBlank(message = "密码为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

    @Schema(description = "是否记住我", defaultValue = "false")
    private Boolean rememberMe = false;
}
