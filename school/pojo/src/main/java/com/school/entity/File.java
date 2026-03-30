package com.school.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Schema(description = "文件ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "对象名称")
    private String objectName;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "上传时间")
    private LocalDateTime uploadTime;

}
