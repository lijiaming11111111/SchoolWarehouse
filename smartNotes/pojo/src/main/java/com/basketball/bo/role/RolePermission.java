package com.basketball.bo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class RolePermission {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "权限列表(path)")
    private List<String> permissions;


}