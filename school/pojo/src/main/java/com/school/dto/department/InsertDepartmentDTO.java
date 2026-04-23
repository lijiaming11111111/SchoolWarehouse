package com.school.dto.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InsertDepartmentDTO {
    @Schema(description = "部门名称")
    @NotNull(message = "部门名称为空")
    private String name;
}
