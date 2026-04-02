package com.smartNotes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smartNotes.enums.role.ApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupApply {
    @Schema(description = "申请ID")
    private String id;

    @Schema(description = "申请群聊id")
    private String groupId;

    @Schema(description = "申请人id")
    private String userId;

    @Schema(description = "申请状态（0=待审核 1=同意 2=拒绝）")
    private ApplyStatus applyStatus;

    @Schema(description = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

}
