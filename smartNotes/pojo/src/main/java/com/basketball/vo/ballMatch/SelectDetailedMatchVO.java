package com.basketball.vo.ballMatch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Time;

@Data
public class SelectDetailedMatchVO {
    @Schema(description = "比赛id")
    private String matchNumber;
    @Schema(description = "赛事名称")
    private String eventName;
    @Schema(description = "队伍A")
    private String teamA;
    @Schema(description = "队伍B")
    private String teamB;
    @Schema(description = "比赛日期")
    private String matchDate;
    @Schema(description = "比赛时间")
    private Time startTime;
    @Schema(description = "场馆名称")
    private String stadiumName;
    @Schema(description = "状态名称")
    private String statusName;
}
