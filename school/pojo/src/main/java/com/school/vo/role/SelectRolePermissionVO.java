package com.school.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SelectRolePermissionVO {
    @Schema(description = "角色ID")
    private String id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "权限ID列表")
    private List<SelectRolePermissionIdVO> permissionIdList;
}
