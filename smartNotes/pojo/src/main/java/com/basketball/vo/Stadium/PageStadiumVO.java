package com.basketball.vo.Stadium;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageStadiumVO {
    @Schema(description = "场馆ID")
    private Long id;
    @Schema(description = "场馆名字",required = true)
    private String name;
    @Schema(description = "场馆地址",required = true)
    private String address;
}
