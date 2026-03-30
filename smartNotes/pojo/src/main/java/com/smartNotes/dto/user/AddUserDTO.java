package com.smartNotes.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddUserDTO {
    @Schema(description = "账号名称")
    @NotBlank(message = "账号名称不能为空")
    private String name;

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
