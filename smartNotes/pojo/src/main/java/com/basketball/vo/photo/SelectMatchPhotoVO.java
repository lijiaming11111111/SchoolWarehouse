package com.basketball.vo.photo;

import com.basketball.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectMatchPhotoVO {
    @Schema(description = "描述")
    private String description;
    @Schema(description = "文件信息")
    private FileDataVO file;
}
