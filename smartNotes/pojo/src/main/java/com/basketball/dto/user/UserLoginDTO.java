package com.basketball.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLoginDTO {

    @Schema(description = "手机号",required = true)
    @NotBlank(message = "账号为空")
    private String telephone;

    @Schema(description = "密码",required = true)
    @NotBlank(message = "密码为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

    @Schema(description = "是否记住我", defaultValue = "false")
    private Boolean rememberMe = false;
}
