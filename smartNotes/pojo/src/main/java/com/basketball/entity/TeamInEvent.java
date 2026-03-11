package com.basketball.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TeamInEvent {
    @Schema(description = "赛事-球队关联id")
    private Long id;
    @Schema(description = "赛事id")
    private Long eventId;
    @Schema(description = "队伍id")
    private Long teamId;
}
