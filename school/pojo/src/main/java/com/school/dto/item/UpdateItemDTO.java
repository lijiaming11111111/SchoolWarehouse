package com.school.dto.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UpdateItemDTO {
    @Schema(description = "设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String id;

    @Schema(description = "设备名称")
    private String itemName;

    @Schema(description = "设备编号")
    private String itemCode;

    @Schema(description = "分类ID")
    private String itemCategoryId;

    @Schema(description = "规格")
    private String specification;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "当前库存")
    private Long currentStock;

    @Schema(description = "安全库存")
    private Long safeStock;

    @Schema(description = "购买时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyTime;

    @Schema(description = "购置金额")
    private BigDecimal buyPrice;

    @Schema(description = "部门ID")
    private String departmentId;

}
