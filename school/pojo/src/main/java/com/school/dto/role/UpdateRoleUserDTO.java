package com.school.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateRoleUserDTO {
    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String id;

    @Schema(description = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private String roleId;
}
