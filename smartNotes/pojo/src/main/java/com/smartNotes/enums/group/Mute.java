package com.smartNotes.enums.group;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "禁言状态（0.未禁言；1.已禁言）")
public enum Mute {
    /**
     * 禁言状态-未禁言
     */
    UNMUTE(0, "未禁言"),
    /**
     * 禁言状态-已禁言
     */
    MUTE(1, "已禁言");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

}
