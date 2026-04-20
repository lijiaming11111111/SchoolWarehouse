package com.school.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRole {
    @Schema(description = "用户角色关联表")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "角色ID")
    private String roleId;
}
