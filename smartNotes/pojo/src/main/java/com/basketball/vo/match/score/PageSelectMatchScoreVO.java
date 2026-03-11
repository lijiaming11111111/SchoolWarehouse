package com.basketball.vo.match.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author 19625
 */
@Data
public class PageSelectMatchScoreVO {
    @Schema(description = "排名")
    private Long rank;

    @Schema(description = "球员名字")
    private String name;

    @Schema(description = "积分")
    private String point;

    @Schema(description = "赢和输的场次")
    private String winNumber;

    @Schema(description = "净胜分")
    private String sumWinPoint;

    @Schema(description = "事件结束的比赛次数")
    private Long finishMatch;

}
