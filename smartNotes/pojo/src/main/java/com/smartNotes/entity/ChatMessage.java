package com.smartNotes.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    @Schema(description = "消息记录id")
    private String id;

    @Schema(description = "群聊时的目标群组 ID，私聊时为NULL")
    private String groupId;

    @Schema(description = "发送者用户ID")
    private String senderId;

    @Schema(description = "私聊时的接收者用户 ID，群聊时为NULL")
    private String receiverId;

    @Schema(description = "聊天内容")
    private String content;

    @Schema(description = "消息发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "文件ID")
    private String fileId;
}
