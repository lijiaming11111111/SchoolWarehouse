package com.smartNotes.entity;

import com.smartNotes.enums.group.Mute;
import com.smartNotes.enums.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMember {
    @Schema(description = "成员记录唯一 ID")
    private String id;

    @Schema(description = "所属群组 ID")
    private String groupId;

    @Schema(description = "成员用户 ID")
    private String userId;

    @Schema(description = "成员角色：默认 1 = 普通成员，2 = 管理员 ，3= 群主")
    private Role role;

    @Schema(description = "加入群组的时间")
    private LocalDateTime joinTime;

    @Schema(description = "禁言状态（0.未禁言；1.已禁言）")
    private Mute mute;

    @Schema(description = "禁言结束时间")
    private LocalDateTime muteEndTime;


}
