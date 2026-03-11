package com.basketball.vo.player;

import com.basketball.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class PlayerVO {
    @Schema(description = "球员ID")
    private Long id;
    
    @Schema(description = "球员姓名")
    private String name;
    
    @Schema(description = "出生日期")
    private Date dateOfBirth;

    @Schema(description = "体重")
    private Integer weight;

    @Schema(description = "身高")
    private Integer height;

    @Schema(description = "球衣号码")
    private Integer shirtNumber;


    @Schema(description = "球队ID")
    private Long teamId;

    @Schema(description = "照片")
    private FileDataVO photo;

    @Schema(description = "位置名称")
     private String position;
}