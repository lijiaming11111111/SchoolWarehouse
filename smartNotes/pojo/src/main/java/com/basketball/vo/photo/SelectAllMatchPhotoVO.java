package com.basketball.vo.photo;

import com.basketball.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectAllMatchPhotoVO {
    @Schema(description = "比赛-照片文件关联id")
    private Long id;
    @Schema(description = "照片文件")
    private FileDataVO file;
}
