package com.school.dto.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReturnItemDTO {
    @Schema(description = "设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String id;

    @Schema(description = "备注")
    private String remark;
}
