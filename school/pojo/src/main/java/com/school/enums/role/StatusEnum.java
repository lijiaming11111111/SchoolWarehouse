package com.school.enums.role;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "角色状态枚举类(1,正常 0,禁用)")
public enum StatusEnum{
    /**
     * 角色状态-禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 角色状态-正常
     */
    NORMAL(1, "正常");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;


}
