package com.school.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "性别枚举对象(1女,0男)")
public enum Gender {

    /**
     * 用户账号状态-正常
     */
    WOMAN(1, "女"),
    /**
     * 用户账号状态-禁用
     */
    MAN(0, "男");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;
}
