package com.basketball.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Team {
    @Schema(description = "球队ID")
    private Long id;
    @Schema(description = "球队名称", required = true)
    private String name;
    @Schema(description = "联系电话", required = true)
    private String contactPhone;
    @Schema(description = "球队地址", required = true)
    private String address;
    @Schema(description = "球队Logo")
    private Long logo;
    @Schema(description = "成立日期")
    private LocalDate establishedDate;
}
