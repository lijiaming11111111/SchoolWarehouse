package com.school.vo.item.flow;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageSelectItemFlowVO {
    @Schema(description = "库存流水记录ID")
    private String id;

    @Schema(description = "设备ID")
    private String itemId;

    @Schema(description = "设备名称")
    private String itemName;

    @Schema(description = "操作人ID")
    private String userId;

    @Schema(description = "操作人姓名")
    private String userName;

    @Schema(description = "类型(1=入库 2=出库 3=借用 4=归还)")
    private Integer flowType;

    @Schema(description = "变动数量")
    private Long quantity;

    @Schema(description = "变动前库存")
    private Long beforeStock;

    @Schema(description = "变动后库存")
    private Long afterStock;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
