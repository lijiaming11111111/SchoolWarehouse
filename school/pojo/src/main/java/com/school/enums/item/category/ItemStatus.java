package com.school.enums.item.category;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "设备状态(1=正常 2=借用中 3=维修 4=报废)")
public enum ItemStatus {

    /**
     * 设备状态-正常
     */
    NORMAL(1, "正常"),

    /**
     * 设备状态-借用中
     */
    BORROW(2, "借用中"),
    /**
     * 设备状态-维修
     */
    REPAIR(3, "维修"),
    /**
     * 设备状态-报废
     */
    SCRAP(4, "报废");

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;
}
