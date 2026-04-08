package com.school.enums.permission;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "权限类型枚举对象(0=菜单 1=按钮)")
public enum PermissionType {
    /**
     * 权限类型枚举对象-菜单
     */
    MENU(0, "菜单"),
    /**
     * 权限类型枚举对象-按钮
     */
    BUTTON(1, "按钮");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

}
