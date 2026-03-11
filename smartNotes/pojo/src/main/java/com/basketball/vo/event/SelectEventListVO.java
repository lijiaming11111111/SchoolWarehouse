package com.basketball.vo.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SelectEventListVO {
    @Schema(description = "赛事id")
    private Long id;
    @Schema(description = "赛事名称")
    private String name;
    @Schema(description = "开始时间")
    private LocalDate startDate;
    @Schema(description = "结束时间")
    private LocalDate endDate;
    @Schema(description = "赛事类型")
    private String eventType;
    @Schema(description = "赛事状态")
    private String status;
}
