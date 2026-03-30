package com.school.dto.user;

import com.school.enums.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class AddUserDTO {
    @Schema(description = "用户姓名", required = true)
    @NotBlank(message = "用户姓名为空")
    private String userName;

    @Schema(description = "账号", required = true)
    @NotBlank(message = "账号为空")
    private String userAccount;

    @Schema(description = "性别枚举对象(1女,0男)", required = true)
    @NotNull(message = "性别枚举对象(1女,0男)为空")
    private Gender gender;

    @Schema(description = "联系电话", required = true)
    @NotBlank(message = "联系电话为空")
    @Size(max = 11, message = "联系电话长度超过限制")
    private String telephone;

    @Schema(description = "密码", required = true)
    @NotBlank(message = "用户密码为空")
    @Size(min = 8, max = 32, message = "请输入8~32位密码")
    private String password;

    @Schema(description = "邮箱", required = true)
    @NotBlank(message = "邮箱为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "部门id", required = true)
    @NotBlank(message = "部门id为空")
    private String departmentId;
}
