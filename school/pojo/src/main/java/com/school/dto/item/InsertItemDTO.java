package com.school.dto.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InsertItemDTO {
    @Schema(description = "设备名称")
    @NotBlank(message = "设备名称不能为空")
    private String itemName;

    @Schema(description = "设备编号")
    @NotBlank(message = "设备编号不能为空")
    private String itemCode;

    @Schema(description = "分类ID")
    @NotBlank(message = "分类ID不能为空")
    private String itemCategoryId;

    @Schema(description = "规格")
    @NotBlank(message = "规格不能为空")
    private String specification;

    @Schema(description = "单位")
    @NotBlank(message = "单位不能为空")
    private String unit;

    @Schema(description = "仓库ID")
    @NotBlank(message = "仓库ID不能为空")
    private String warehouseId;

    @Schema(description = "当前库存")
    @NotNull(message = "当前库存不能为空")
    private Long currentStock;

    @Schema(description = "安全库存")
    @NotNull(message = "安全库存不能为空")
    private Long safeStock;

    @Schema(description = "购买时间")
    @NotNull(message = "购买时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyTime;

    @Schema(description = "购置金额")
    @NotNull(message = "购置金额不能为空")
    private BigDecimal buyPrice;

    @Schema(description = "使用部门")
    @NotBlank(message = "使用部门不能为空")
    private String useDepartment;

}
