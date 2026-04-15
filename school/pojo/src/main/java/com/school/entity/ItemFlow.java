package com.school.entity;

import com.school.enums.item.flow.FlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ItemFlow {
    /**
     * 库存流水记录ID
     */
    @Schema(description = "库存流水记录ID")
    private String id;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID")
    private String itemId;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String itemName;

    /**
     * 类型(1=入库 2=出库 3=借用 4=归还)
     */
    @Schema(description = "类型(1=入库 2=出库 3=借用 4=归还)")
    private FlowType flowType;

    /**
     * 变动数量 (+增加-减少)
     */
    @Schema(description = "变动数量")
    private Long quantity;

    /**
     * 变动前库存
     */
    @Schema(description = "变动前库存")
    private Long beforeStock;

    /**
     * 变动后库存
     */
    @Schema(description = "变动后库存")
    private Long afterStock;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 操作时间
     */
    @Schema(description = "操作时间")
    private LocalDate createTime;


}