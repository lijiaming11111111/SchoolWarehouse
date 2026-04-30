package com.school.dto.user;

import com.school.enums.user.Gender;
import com.school.enums.user.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateUserDTO {
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "未传入需要修改的用户")
    private String id;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "性别枚举对象(1女,0男)")
    private Gender gender;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "联系电话")
    @Size(max = 11, message = "联系电话长度超过限制")
    private String telephone;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "用户账号状态（0.禁用；1.正常）")
    private StatusEnum statusEnum;

    @Schema(description = "部门id")
    private String departmentId;
}
