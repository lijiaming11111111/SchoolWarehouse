package com.school.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectPermissionIdVO {
    @Schema(description = "权限ID")
    private String id;
}
