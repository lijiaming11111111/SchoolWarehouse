package com.school.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PutItemDTO {
    @Schema(description = "设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String id;

    @Schema(description = "入库数量")
    @NotNull(message = "入库数量不能为空")
    private Long putStock;
}
