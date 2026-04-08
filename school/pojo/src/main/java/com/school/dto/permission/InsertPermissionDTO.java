package com.school.dto.permission;


import com.school.enums.permission.PermissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InsertPermissionDTO {
    @Schema(description="权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @Schema(description="权限编码")
    @NotBlank(message = "权限编码不能为空")
    private String code;

    @Schema(description ="父权限ID，0为顶级")
    @NotBlank(message = "父权限ID不能为空")
    private String parentId;

    @Schema(description = "权限类型(0=菜单 1=按钮)")
    @NotNull(message = "权限类型不能为空")
    private PermissionType permissionType;
}
