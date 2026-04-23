package com.school.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Department {
    @Schema(description = "部门ID")
    private String id;

    @Schema(description = "部门名称")
    private String name;
}
