package com.basketball.vo.video;

import com.basketball.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectAllVideoVO {
    @Schema(description = "视频ID")
    private String id;

    @Schema(description = "简介")
    private String introduce;

    @Schema(description = "视频文件信息")
    private FileDataVO video;
}
