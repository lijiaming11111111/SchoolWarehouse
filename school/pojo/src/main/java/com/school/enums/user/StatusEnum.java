package com.school.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "用户账号状态（1.正常；2.禁用）")
public enum StatusEnum{
    /**
     * 用户账号状态-禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 用户账号状态-正常
     */
    NORMAL(1, "正常");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;


}
