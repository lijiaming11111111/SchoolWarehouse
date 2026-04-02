package com.smartNotes.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupInfo {
    @Schema(description = "群组id")
    private String id;

    @Schema(description = "群组名称")
    private String groupName;

    @Schema(description = "群主 / 创建者的用户 ID")
    private String ownerId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
