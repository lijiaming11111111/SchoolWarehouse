package com.school.dto.item;

import com.school.enums.item.category.ItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateItemStatusDTO {
    @Schema(description = "设备信息ID")
    private String id;

    @Schema(description = "设备状态")
    private ItemStatus status;
}
