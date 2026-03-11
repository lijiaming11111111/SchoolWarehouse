package com.basketball.vo.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SelectEventVO {
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
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "参赛队伍id列表")
    private List<Long> teamIdList;
}
