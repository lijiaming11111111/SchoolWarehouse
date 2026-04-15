package com.school.dto.warehouse;

import com.school.enums.warehouse.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InsertWarehouseDTO {
    @Schema(description = "仓库名称")
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    @Schema(description = "仓库编号")
    @NotBlank(message = "仓库编号不能为空")
    private String warehouseCode;

    @Schema(description = "仓库位置")
    @NotBlank(message = "仓库位置不能为空")
    private String address;

    @Schema(description = "负责人ID")
    @NotBlank(message = "负责人ID不能为空")
    private String userId;

    @Schema(description = "仓库状态(1=正常0=禁用)")
    @NotBlank(message = "仓库状态不能为空")
    private Status status;

    @Schema(description = "备注")
    private String remark;
}
