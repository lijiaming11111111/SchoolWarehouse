package com.school.entity;

import com.school.enums.user.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Role {
    @Schema(description = "角色ID")
    private String id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "账号状态枚举类(1,正常 0,禁用)")
    private StatusEnum statusEnum;
}
