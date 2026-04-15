package com.school.entity;

import com.school.enums.warehouse.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Warehouse {

    @Schema(description = "仓库ID")
    private String id;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "仓库编号")
    private String warehouseCode;

    @Schema(description = "仓库位置")
    private String address;

    @Schema(description = "负责人ID")
    private String userId;

    @Schema(description = "仓库状态(1=正常0=禁用)")
    private Status status;

    @Schema(description = "备注")
    private String remark;
}
