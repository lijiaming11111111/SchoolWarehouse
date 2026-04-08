package com.school.vo.role;

import com.school.enums.permission.PermissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectRolePermissionIdVO {
    @Schema(description="权限ID")
    private String id;

    @Schema(description="权限名称")
    private String name;

    @Schema(description="权限编码")
    private String code;

    @Schema(description ="父权限ID，0为顶级")
    private String parentId;

    @Schema(description = "权限类型(0=菜单 1=按钮)")
    private PermissionType permissionType;
}
