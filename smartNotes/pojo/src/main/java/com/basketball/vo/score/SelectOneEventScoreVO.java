package com.basketball.vo.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectOneEventScoreVO {
    @Schema(description = "球队积分")
    private Long score;
}
