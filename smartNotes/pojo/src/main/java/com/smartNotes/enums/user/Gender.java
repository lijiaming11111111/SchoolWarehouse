package com.smartNotes.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartNotes.enums.role.ApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "性别（0=男 1=女）")
public enum Gender {
    /**
     * 性别-男
     */
    MALE(0, "男"),
    /**
     * 性别-女
     */
    FEMALE(1, "女");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

    // 根据code获取枚举
    public static Gender getByCode(Integer code) {
        for (Gender status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }
}
