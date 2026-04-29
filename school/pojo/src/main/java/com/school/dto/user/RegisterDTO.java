package com.school.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterDTO {
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "确认密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
