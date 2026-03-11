package com.basketball.vo.ballMatch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;

@Data
public class SelectMatchVO {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "比赛id")
    private String matchNumber;
    @Schema(description = "赛事id")
    private String eventId;
    @Schema(description = "赛事名称")
    private String eventName;
    @Schema(description = "队伍A")
    private String teamA;
    @Schema(description = "队伍A_id")
    private String teamAId;
    @Schema(description = "队伍B")
    private String teamB;
    @Schema(description = "队伍B_id")
    private String teamBId;
    @Schema(description = "比赛日期")
    private String matchDate;
    @Schema(description = "比赛时间")
    private Time startTime;
    @Schema(description = "场馆id")
    private String stadiumId;
    @Schema(description = "状态id")
    private Integer statusId;
}
