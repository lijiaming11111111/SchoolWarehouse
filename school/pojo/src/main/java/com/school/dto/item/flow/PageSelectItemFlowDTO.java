package com.school.dto.item.flow;

import com.school.enums.item.flow.FlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageSelectItemFlowDTO {

    @Schema(description = "设备名称")
    private String itemName;

    @Schema(description = "类型(1=入库 2=出库 3=借用 4=归还)")
    private FlowType flowType;

    @Schema(description = "变动数量（+增加 -减少）")
    private Long quantity;

    @Schema(description = "变动前库存")
    private Long beforeStock;

    @Schema(description = "变动后库存")
    private Long afterStock;

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
