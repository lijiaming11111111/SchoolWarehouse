package com.basketball.vo.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectEventScoreVO {
    @Schema(description = "排名")
    private Long rank;
    @Schema(description = "球队积分")
    private Long score;
    @Schema(description = "球队名称")
    private String name;
}
