package com.school.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordDTO {
    @Schema(description = "需要重置密码的用户ID", required = true)
    @NotNull(message = "未传入需要重置密码的用户")
    private Long userId;

    @Schema(description = "新密码", required = true)
    @NotBlank(message = "新密码为空")
    @Size(min = 8, max = 32, message = "请输入8~32位密码")
    private String newPassword;
}
