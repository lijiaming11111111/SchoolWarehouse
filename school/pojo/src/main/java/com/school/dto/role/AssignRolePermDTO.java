package com.school.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AssignRolePermDTO {
    @Schema(description = "id")
    @NotBlank(message = "角色ID为空")
    private String id;

    @Schema(description = "删除权限id列表")
    private List<String> deletePermissionIdList;

    @Schema(description = "添加权限id列表")
    private List<String> addPermissionIdList;
}
