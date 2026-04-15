package com.school.dto.role;

import com.school.enums.user.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InsertRoleDTO {
    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @Schema(description = "账号状态枚举类(1,正常 0,禁用)")
    @NotBlank(message = "账号状态不能为空")
    private StatusEnum statusEnum;
}
