package com.school.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.sql.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StockItemDTO {
    @Schema(description = "设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String id;

    @Schema(description = "出库数量")
    @NotNull(message = "出库数量不能为空")
    private Long stockStock;
}
