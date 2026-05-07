package com.school.dto.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BorrowItemDTO {
    @Schema(description = "设备ID")
    @NotBlank(message = "设备ID不能为空")
    private String id;

    @Schema(description = "借用数量")
    @NotNull(message = "借用数量不能为空")
    private Long borrowNumber;

    @Schema(description = "归还时间(yyyy-MM-dd)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "归还时间不能为空")
    private LocalDate returnTime;

    @Schema(description = "备注")
    private String remark;
}
