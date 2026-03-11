package com.basketball.vo.player;

import com.basketball.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
@Data
public class SelectPlayerVO {
    @Schema(description = "球员id")
    private String id;

    @Schema(description = "名")
    private String firstName;

    @Schema(description = "姓")
    private String lastName;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "出生日期")
    private Date dateOfBirth;

    @Schema(description = "体重")
    private Integer weight;

    @Schema(description = "身高")
    private Integer height;

    @Schema(description = "球衣号码")
    private Integer shirtNumber;

    @Schema(description = "球员职位(1小前锋，2大前锋，3中锋，4得分后卫，5控球后卫)")
    private Integer positionId;

    @Schema(description = "球队ID")
    private String teamId;

    @Schema(description = "照片")
    private FileDataVO photo;
}
