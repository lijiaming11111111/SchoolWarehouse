package com.school.vo.deparrtment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SelectDepartmentVO {
    @Schema(description = "部门ID")
    private String id;

    @Schema(description = "部门名称")
    private String name;
}
