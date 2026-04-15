package com.school.dto.warehouse;

import com.school.enums.warehouse.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageSelectWarehouseDTO {
    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Schema(description = "仓库位置")
    private String address;

    @Schema(description = "负责人ID")
    private String userId;

    @Schema(description = "仓库状态(1=正常 0=禁用)")
    private Status status;

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
