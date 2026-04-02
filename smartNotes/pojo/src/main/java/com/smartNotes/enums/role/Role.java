package com.smartNotes.enums.role;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "角色类型（1.普通用户；2.管理员,3.群主）")
public enum Role {

    /**
     * 角色类型-普通用户
     */
    NORMAL(1, "普通用户"),
    /**
     * 角色类型-管理员
     */
    ADMIN(2, "管理员"),
    /**
     * 角色类型-群主
     */
    GROUP_OWNER(3, "群主");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;


}
