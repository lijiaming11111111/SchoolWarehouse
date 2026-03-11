package com.basketball.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Video {
    @Schema(description = "视频ID")
    private String id;

    @Schema(description = "简介")
    private String introduce;

    @Schema(description = "观看人数")
    private Long viewCount;

    @Schema(description = "详情")
    private String particulars;

    @Schema(description = "发布者")
    private String publisher;

    @Schema(description = "发布日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @Schema(description = "视频文件ID")
    private String videoFileId;
}
