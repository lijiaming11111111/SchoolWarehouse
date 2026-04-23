package com.school.dto.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateDepartmentDTO {
    @Schema(description = "部门ID")
    @NotNull(message = "部门ID为空")
    private String id;

    @Schema(description = "部门名称")
    private String name;
}
