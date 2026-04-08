package com.school.dto.role;

import com.school.enums.user.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageSelectRoleDTO {
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色状态枚举类(1,正常 0,禁用)")
    private StatusEnum statusEnum;

    @Schema(description = "页码", defaultValue = "1",required = true)
    @NotNull(message = "页码为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer page;

    @Schema(description = "每页显示记录数", defaultValue = "10",required = true)
    @NotNull(message = "页数为空")
    @Min(value = 1, message = "页数不能小于1")
    @Max(value = 50, message = "页数不能超过50")
    private Integer pageSize;
}
